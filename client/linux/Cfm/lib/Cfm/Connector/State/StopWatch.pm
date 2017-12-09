package Cfm::Connector::State::StopWatch;
use strict;
use warnings FATAL => 'all';
use Moo;

use Time::HiRes qw/time/;

has trip_start => (
        is => 'rw',
    );
has total => (
        is      => 'rw',
        default => sub {0},
    );
# 0: stopped
# 1: running
has watch_state => (
        is      => 'rw',
        default => sub {0},
    );


sub start_at {
    my ($self, $t) = @_;

    return 0 if $self->watch_state == 1;
    $self->trip_start($t);
    $self->watch_state(1);
    1;
}

sub start {
    my ($self) = @_;

    $self->start_at(time());
}

sub stop_at {
    my ($self, $t) = @_;

    return 0 if $self->watch_state == 0;
    $self->total($self->total + ($t - $self->trip_start));
    $self->watch_state(0);
    1;
}

sub stop {
    my ($self) = @_;

    $self->stop_at(time());
}

sub reset {
    my ($self) = @_;

    $self->stop;
    my $total = $self->total;
    $self->total(0);
    $total;
}

1;
