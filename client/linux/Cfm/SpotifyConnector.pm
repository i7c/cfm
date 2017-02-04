package Cfm::SpotifyConnector;
use strict;
use warnings FATAL => 'all';

use Moo;
use Net::DBus;
use Net::DBus::Reactor;
use Data::Dumper;

use Cfm::PlayerStateMachine;

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

sub listen {
    my ($self) = @_;

    my $bus = Net::DBus->session;
    my $spotify_service = $bus->get_service("org.mpris.MediaPlayer2.spotify");
    my $mpris_interface = $spotify_service->get_object("/org/mpris/MediaPlayer2", "org.freedesktop.DBus.Properties");
    $mpris_interface->connect_to_signal("PropertiesChanged", sub {
            my ($source, $rawdata) = @_;

            my $metadata = $rawdata->{Metadata};
            my $data = {
                artist => $metadata->{"xesam:artist"},
                title  => $metadata->{"xesam:title"},
                album  => $metadata->{"xesam:album"},
                length => $metadata->{"mpris:length"} / 1000000 # in seconds
            };

            if ($rawdata->{PlaybackStatus} eq "Playing") {
                $self->psm->play($data, $self, $metadata);
            } elsif ($rawdata->{PlaybackStatus} eq "Paused") {
                $self->psm->pause($data, $self, $metadata);
            }
        });
    my $reactor = Net::DBus::Reactor->main();
    $reactor->run();
}

sub started {
    my ($artists, $title, $album, $length, $passed_time, $self, $metadata) = @_;
    my $artist = join(", ", @$artists);

    print "Started: $artist - $title\n";
}

sub completed {
    my ($artists, $title, $album, $length, $passed_time, $self, $metadata) = @_;
    my $artist = join(", ", @$artists);

    print "Completed: $artist - $title ($passed_time of $length seconds)\n";
}

sub canceled {
    my ($artists, $title, $album, $length, $passed_time, $self, $metadata) = @_;
    my $artist = join(", ", @$artists);

    print "Canceled: $artist - $title ($passed_time of $length seconds)\n";
}

sub resumed {
    my ($artists, $title, $album, $length, $passed_time, $self, $metadata) = @_;
    my $artist = join(", ", @$artists);

}

1;