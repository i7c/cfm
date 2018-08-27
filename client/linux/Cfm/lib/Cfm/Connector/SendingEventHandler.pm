package Cfm::Connector::SendingEventHandler;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Connector::PlayerEventHandler';
use Log::Any qw/$log/;

use Cfm::Autowire;
use Cfm::Playback::PlaybackService;
use Sys::Hostname;

has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub track_started {
    my ($self, $time, $track) = @_;

    $log->info("START " . (join ", ", $track->{artists}->@*) . " - $track->{title} ($track->{release})");

    my $playback = Cfm::Playback::Playback->new(
        artists        => $track->{artists},
        recordingTitle => $track->{title},
        releaseTitle   => $track->{release},
        trackLength    => int($track->{length_s}),
    );

    $self->playback_service->set_now_playing($playback);
}

sub track_ended {
    my ($self, $time, $elapsed, $track) = @_;

    $log->info("END " . (join ", ", $track->{artists}->@*) . " - $track->{title} ($track->{release})");

    my $playback = Cfm::Playback::Playback->new(
        artists        => $track->{artists},
        recordingTitle => $track->{title},
        releaseTitle   => $track->{release},
        discNumber     => $track->{discNumber},
        trackNumber    => $track->{trackNumber},
        trackLength    => int($track->{length_s}),
        playTime       => $elapsed,
        source         => hostname,
    );

    $self->playback_service->create_playback($playback);
}

1;
