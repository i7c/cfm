package Cfm::Connector::PlayerEventHandler;
use strict;
use warnings FATAL => 'all';
use Moo::Role;
use Log::Any qw/$log/;

sub track_started {
    my ($self, $time, $track) = @_;

    $log->info("START " . (join ", ", $track->{artists}->@*) . " - $track->{title} ($track->{release})");
}

sub track_paused {
    my ($self, $time, $elapsed, $track) = @_;

    $log->info("PAUSE " . (join ", ", $track->{artists}->@*) . " - $track->{title} ($track->{release})");
}

sub track_resumed {
    my ($self, $time, $track) = @_;

    $log->info("RESUME " . (join ", ", $track->{artists}->@*) . " - $track->{title} ($track->{release})");
}

sub track_ended {
    my ($self, $time, $elapsed, $track) = @_;

    $log->info("END " . (join ", ", $track->{artists}->@*) . " - $track->{title} ($track->{release})");
}

sub track_cancelled {
    my ($self, $time, $elapsed, $track) = @_;

    $log->info("CANCEL " . (join ", ", $track->{artists}->@*) . " - $track->{title} ($track->{release})");
}

1;
