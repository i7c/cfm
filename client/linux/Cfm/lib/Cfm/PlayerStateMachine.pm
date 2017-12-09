package Cfm::PlayerStateMachine;

use strict;
use warnings FATAL => 'all';
use Moo;
use Cfm::Autowire;

use Cfm::Config;
use Data::Dumper;
use Time::HiRes qw/time/;

has config => singleton 'Cfm::Config';

has metadata => (
        is      => 'rw',
        default => sub {{
            artists => [],
            title   => "",
            album   => "",
            length  => - 1,
        }},
    );
has state => (
        is      => 'rw',
        default => sub {- 1}
    );
has start_time => (is => 'rw');
has passed_time => (
        is      => 'rw',
        default => sub {0}
    );
has threshold => (
        is      => 'lazy',
        default => sub {$_[0]->config->get_option("threshold") / 100.0}
    );
has cb_playback_completed => (is => 'rw');
has cb_playback_canceled => (is => 'rw');
has cb_playback_started => (is => 'rw');
has cb_playback_resumed => (is => 'rw');

# Moves the machine into "playing" state. If the transition is from "stopped", a "begin of track" event will be fired
#  and times will be reset accordingly. If transition is from "paused" or "playing", the outcome depends on whether
# the track changes with this event: if yes, "end of track" will be fired as well as "begin of track", if no, a
# "resume track" event is fired. Regardless of track change: the passed time will only be updated if state was not
# "pause" before (in which case the pause transition already updated passed time). Start time will always be updated.
sub play {
    my $self = shift;
    my $data = shift;
    my @user_args = @_;

    my $now = time() * 1000;

    if ($self->state == - 1) {
        # transition: stopped -> playing
        $self->_set_playing($now);
        $self->_set_new_track($data);
        $self->_emit_begin_of_track_event(\@user_args);
        return;
    } elsif ($self->state == 0 || $self->state == 1) {
        # transition: pause -> playing OR still playing
        if ($self->_track_changed($data)) {
            $self->_update_passed_time($now) if ($self->state == 1);
            $self->_emit_end_of_track_event(\@user_args);
            $self->_set_playing($now);
            $self->_set_new_track($data);
            $self->_emit_begin_of_track_event(\@user_args);
        } else {
            $self->_set_playing($now) if ($self->state == 0);
            $self->_emit_resume_track_event(\@user_args);
        }
        return;
    }
    die "this should not happen: invalid state in PlayerStateMachine.";
}

# Shift state machine into "paused" state. If the transition is # from "playing", the times will be updated. If this
# event involves a track change, "end of track" and "begin of track" events will be fired.
sub pause {
    my $self = shift;
    my $data = shift;
    my @user_args = @_;

    my $now = time() * 1000;

    if ($self->state == 1) {
        # transition: playing -> pause
        $self->_update_passed_time($now);
        $self->_set_paused;
    } elsif ($self->state == 0) {
        # transition: paused -> paused
        # do nothing
    } elsif ($self->state == - 1) {
        # transition: stopped -> paused
        $self->_set_paused;
    } else {
        die "this should not happen: invalid state in PlayerStateMachine.";
    }

    if ($self->_track_changed($data)) {
        $self->_emit_end_of_track_event(\@user_args);
        $self->_set_new_track($data);
        $self->_emit_begin_of_track_event(\@user_args);
        return;
    } else {
        return;
    }
    die "this should not happen";
}

sub stop {
    die "unsupported.";
}

# update the passed time using the moment "now" and the start time of the playing period. This function is NOT
# idempotent. You must reset the start time afterwards.
sub _update_passed_time {
    my ($self, $now) = @_;

    $self->passed_time($self->passed_time + ($now - $self->start_time));
}

# sets internal state to "playing"; this involves setting the start time for the playing period.
sub _set_playing {
    my ($self, $now) = @_;

    $self->start_time($now);
    $self->state(1);
}

# sets internal state to "paused"
sub _set_paused {
    my ($self) = @_;

    $self->state(0);
}

# return true if track changed
sub _track_changed {
    my ($self, $data) = @_;

    return 1 if (_arrays_differ($self->metadata->{artist}, $data->{artist})
        || ($self->metadata->{title} ne $data->{title})
        || ($self->metadata->{album} ne $data->{album}));
    return 0;
}

sub _arrays_differ {
    my ($a1, $a2) = @_;

    return 1 if (defined $a1 xor defined $a2);
    return 0 if (!defined $a1 && !defined $a2);
    return 1 if (scalar @$a1 != scalar @$a2);

    for (my $i = 0; $i < scalar @$a1; $i++) {
        return 1 if ($a1->[$i] ne $a2->[$i]);
    }
    return 0;
}

# Sets the new track and resets passed time
sub _set_new_track {
    my ($self, $data) = @_;

    $self->passed_time(0);
    $self->metadata($data);
}

# returns true if state machine is not yet in a consistent state
sub _preinitialised {
    my ($self) = @_;

    return 1 if ($self->metadata->{length} < 0);
    return 0;
}

# emits an event for a track that is not played anymore; depending on the actual passed time this might result in a
# "completed" event or a "canceled" event.
sub _emit_end_of_track_event {
    my ($self, $user_args) = @_;

    return if $self->_preinitialised;
    if (($self->passed_time / $self->metadata->{length}) >= $self->threshold) {
        if (defined $self->cb_playback_completed) {
            $self->passed_time($self->metadata->{length}) if ($self->passed_time > $self->metadata->{length});
            $self->cb_playback_completed->($self->metadata, $self->passed_time, @$user_args)
        }
    } else {
        if (defined $self->cb_playback_canceled) {
            $self->cb_playback_canceled->($self->metadata, $self->passed_time, @$user_args)
        }
    }
}

# emits an event if a new track is played
sub _emit_begin_of_track_event {
    my ($self, $user_args) = @_;

    return if $self->_preinitialised;
    if (defined $self->cb_playback_started) {
        $self->cb_playback_started->($self->metadata, $self->passed_time, @$user_args);
    }
}

# emits an event if a track is played, that has already been playing before. This might happen if play is called in
# "playing" or "paused" state, regardless, but no track change occured.
sub _emit_resume_track_event {
    my ($self, $user_args) = @_;

    return if $self->_preinitialised;
    if (defined $self->cb_playback_resumed) {
        $self->cb_playback_resumed->($self->metadata, $self->passed_time, @$user_args);
    }
}

1;
