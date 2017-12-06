package Cfm::Connector::Mpris2;
use strict;
use warnings FATAL => 'all';
use Moo;
use Log::Any qw/$log/;

use Net::DBus;
use Net::DBus::Reactor;
use Data::Dumper;
use Data::Printer;

use Cfm::PlayerStateMachine;
use Cfm::SavePlaybackDto;

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

    $log->debug("Get DBus session");
    my $bus = Net::DBus->session;
    $log->debug("Get DBus service " . $self->dbus_name);
    my $spotify_service = $bus->get_service($self->dbus_name);
    $log->debug("Get DBus interface");
    my $main_interface = $spotify_service->get_object("/org/mpris/MediaPlayer2",
        "org.freedesktop.DBus.Properties");

    my $state = "Playing";

    $log->debug("Hook to signal PropertiesChanged");
    $main_interface->connect_to_signal("PropertiesChanged", sub {
            my ($player, $rawdata) = @_;

            $log->info("Received signal.");
            $log->debug(Dumper(@_));

            if (defined $rawdata->{PlaybackStatus}) {
                $log->info("Received playback status update: " . $rawdata->{PlaybackStatus});
                $state = $rawdata->{PlaybackStatus};
            }
            if (defined $rawdata->{Metadata}) {
                my $metadata = $rawdata->{Metadata};
                if (!defined $metadata->{"xesam:title"}
                    || !defined $metadata->{"mpris:length"}
                    || $metadata->{"xesam:title"} eq ""
                    || $metadata->{"mpris:length"} == 0) {
                    $log->warn("Ignoring bogus signal from mpris2.");
                    $log->warn(Dumper($metadata));
                    return;
                }
                for my $artist ($metadata->{"xesam:artist"}->@*) {
                    utf8::decode($artist);
                }
                utf8::decode($metadata->{"xesam:title"});
                utf8::decode($metadata->{"xesam:album"});
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
    $log->info("Connection to DBus established. Listening ...");
    my $reactor = Net::DBus::Reactor->main();
    $reactor->run();
}

sub started {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    $log->debug(Dumper($metadata));
    $log->debug("Passed time: $passed_time");
    $log->info("Started: $artist - $metadata->{title}");
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

    $log->debug(Dumper($metadata));
    $log->debug("Passed time: $passed_time");

    $log->info("Completed: $artist - $metadata->{title} ($passed_time of $metadata->{length} ms)");
    $log->info("Sending playback to server ... ");
    $self->client->create_playback($create_playback);
}

sub canceled {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    $log->debug(Dumper($metadata));
    $log->debug("Passed time: $passed_time");
    $log->info("Canceled: $artist - $metadata->{title} ($passed_time of $metadata->{length} ms)");
}

sub resumed {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    $log->debug(Dumper($metadata));
    $log->debug("Passed time: $passed_time");
    $log->info("Resumed: $artist - $metadata->{title} ($passed_time of $metadata->{length} ms)");
}

1;
