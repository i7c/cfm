package Cfm::Mpris2Connector;
use strict;
use warnings FATAL => 'all';

use Moo;
use Net::DBus;
use Net::DBus::Reactor;
use Data::Dumper;

use Cfm::PlayerStateMachine;
use Cfm::CreatePlayback;

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

    my $bus = Net::DBus->session;
    my $spotify_service = $bus->get_service($self->dbus_name);
    my $mpris_interface = $spotify_service->get_object("/org/mpris/MediaPlayer2", "org.freedesktop.DBus.Properties");
    $mpris_interface->connect_to_signal("PropertiesChanged", sub {
            my ($source, $rawdata) = @_;

            my $metadata = $rawdata->{Metadata};
            my $data = {
                artists => $metadata->{"xesam:artist"},
                title  => $metadata->{"xesam:title"},
                album  => $metadata->{"xesam:album"},
                length => $metadata->{"mpris:length"} / 1000000, # in seconds
                trackNumber => $metadata->{"xesam:trackNumber"},
                discNumber => $metadata->{"xesam:discNumber"},
                rawdata => $metadata
            };

            if ($rawdata->{PlaybackStatus} eq "Playing") {
                $self->psm->play($data, $metadata, $self);
            } elsif ($rawdata->{PlaybackStatus} eq "Paused") {
                $self->psm->pause($data, $metadata, $self);
            }
        });
    print "Listening.\n";
    my $reactor = Net::DBus::Reactor->main();
    $reactor->run();
}

sub started {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    print "Started: $artist - $metadata->{title}\n";
}

sub completed {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    my $create_playback = Cfm::CreatePlayback->new(
        title   => $metadata->{title},
        artists => $metadata->{artists},
        album   => $metadata->{album},
        length  => $metadata->{length},
        trackNumber  => $metadata->{trackNumber},
        discNumber  => $metadata->{discNumber},
    );

    $self->client->create_playback($create_playback);
    print "Completed: $artist - $metadata->{title} ($passed_time of $metadata->{length} seconds)\n";
}

sub canceled {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});

    print "Canceled: $artist - $metadata->{title} ($passed_time of $metadata->{length} seconds)\n";
}

sub resumed {
    my ($metadata, $passed_time, $self) = @_;
    my $artist = join(", ", @{$metadata->{artists}});
    print "Resumed: $artist - $metadata->{title} ($passed_time of $metadata->{length} seconds)\n";
}

1;