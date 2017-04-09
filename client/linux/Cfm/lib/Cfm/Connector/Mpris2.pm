package Cfm::Connector::Mpris2;
use strict;
use warnings FATAL => 'all';

use Moo;
use Net::DBus;
use Net::DBus::Reactor;
use Data::Dumper;
use Data::Printer;
use Log::Log4perl;

use Cfm::PlayerStateMachine;
use Cfm::SavePlaybackDto;

my $logger = Log::Log4perl->get_logger("cfm");

has psm => (
        is      => 'rw',
        default => sub {
            Cfm::PlayerStateMachine->new(
                cb_playback_started   => \&started,
                cb_playback_canceled  => \&canceled,
                cb_playback_completed => \&completed,
                cb_playback_resumed   => \&resumed
            );
        }
    );

has client => (is => 'ro');

has dbus_name => (is => 'ro');

sub listen {
    my ($self) = @_;

    $logger->debug("Get DBus session");
    my $bus = Net::DBus->session;
    $logger->debug("Get DBus service " . $self->dbus_name);
    my $spotify_service = $bus->get_service($self->dbus_name);
    $logger->debug("Get DBus interface");
    my $main_interface = $spotify_service->get_object("/org/mpris/MediaPlayer2",
        "org.freedesktop.DBus.Properties");

    my $state = "Playing";

    $logger->debug("Hook to signal PropertiesChanged");
    $main_interface->connect_to_signal("PropertiesChanged", sub {
            my ($player, $rawdata) = @_;

            $logger->info("Received signal.");
            $logger->debug(Dumper(@_));

            if (defined $rawdata->{PlaybackStatus}) {
                $logger->info("Received playback status update: " . $rawdata->{PlaybackStatus});
                $state = $rawdata->{PlaybackStatus};
            }
            if (defined $rawdata->{Metadata}) {
                my $metadata = $rawdata->{Metadata};
                if (!defined $metadata->{"xesam:title"}
                    || !defined $metadata->{"mpris:length"}
                    || $metadata->{"xesam:title"} eq ""
                    || $metadata->{"mpris:length"} == 0) {
                    $logger->warn("Ignoring bogus signal from mpris2.");
                    $logger->warn(Dumper($metadata));
                    return;
                }
                my $data = {
                    artists     => $metadata->{"xesam:artist"},
                    title       => $metadata->{"xesam:title"},
                    album       => $metadata->{"xesam:album"},
                    length      => $metadata->{"mpris:length"} / 1000, # in ms
                    trackNumber => $metadata->{"xesam:trackNumber"},
                    rawdata     => $metadata
                };

                if ($state eq "Playing") {
                    $self->psm->play($data, $self);
                } elsif ($state eq "Paused") {
                    $self->psm->pause($data, $self);
                }
            }
            return;
        });
    $logger->info("Connection to DBus established. Listening ...");
    my $reactor = Net::DBus::Reactor->main();
    $reactor->run();
}

sub started {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    $logger->debug(Dumper($metadata));
    $logger->debug("Passed time: $passed_time");
    $logger->info("Started: $artist - $metadata->{title}");
}

sub completed {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    my $create_playback = Cfm::SavePlaybackDto->new(
        title       => $metadata->{title},
        artists     => $metadata->{artists},
        album       => $metadata->{album},
        length      => $metadata->{length},
        trackNumber => $metadata->{trackNumber},
        discNumber  => $metadata->{discNumber},
    );

    $logger->debug(Dumper($metadata));
    $logger->debug("Passed time: $passed_time");
    $logger->debug("SavePlaybackDto: " . Dumper($create_playback->_to_hash));

    $logger->info("Completed: $artist - $metadata->{title} ($passed_time of $metadata->{length} ms)");
    $logger->info("Sending playback to server ... ");
    $self->client->create_playback($create_playback);
}

sub canceled {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    $logger->debug(Dumper($metadata));
    $logger->debug("Passed time: $passed_time");
    $logger->info("Canceled: $artist - $metadata->{title} ($passed_time of $metadata->{length} ms)");
}

sub resumed {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    $logger->debug(Dumper($metadata));
    $logger->debug("Passed time: $passed_time");
    $logger->info("Resumed: $artist - $metadata->{title} ($passed_time of $metadata->{length} ms)");
}

1;
