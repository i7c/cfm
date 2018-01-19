package Cfm::Connector::State::TrackState;
use strict;
use warnings FATAL => 'all';
use Moo;

has pre_state => (
        is      => 'rw',
        default => sub {1},
    );
has title => (is => 'rw');
has artists => (is => 'rw');
has release => (is => 'rw');


sub update {
    my ($self, $artists, $title, $release) = @_;

    my $changed = 0;
    $changed = 1 if !$self->pre_state && (($self->title ne $title)
        || ($self->release ne $release)
        || _arrays_differ($self->artists, $artists));

    $self->pre_state(0);
    $self->title($title);
    $self->artists($artists);
    $self->release($release);
    return $changed;
}

sub clear {
    my ($self) = @_;

    $self->pre_state(1);
    $self->title(undef);
    $self->artists(undef);
    $self->release(undef);
}

sub _arrays_differ {
    my ($a1, $a2) = @_;

    return 1 if defined $a1 xor defined $a2;
    return 0 unless defined $a1;
    return 1 if scalar @$a1 != scalar @$a2;

    for (my $i = 0; $i < scalar @$a1; $i++) {
        return 1 if $a1->[$i] ne $a2->[$i];
    }
    return 0;
}

1;
