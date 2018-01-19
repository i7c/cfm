package Cfm::Connector::State::PlayerStateMachine;
use strict;
use warnings FATAL => 'all';
use Moo;
use Log::Any qw/$log/;

use Cfm::Autowire;
use Cfm::Connector::State::StopWatch;
use Cfm::Connector::State::TrackState;
use Cfm::Connector::SendingEventHandler;

has watch => autowire 'Cfm::Connector::State::StopWatch';
has track => autowire 'Cfm::Connector::State::TrackState';
has handler => autowire 'Cfm::Connector::SendingEventHandler' => 'Cfm::Connector::PlayerEventHandler';
has config => singleton 'Cfm::Config';

has last_track => (is => 'rw');
has threshold => (
        is      => 'lazy',
        default => sub {
            $_[0]->config->get_option("threshold");
        }
    );
has gap_to_complete => (
        is      => 'lazy',
        default => sub {
            $_[0]->config->get_option("gap-to-complete");
        }
    );

sub play {
    my ($self, $time, $track) = @_;

    my $track_changed = $self->track->update($track->{artists}, $track->{title}, $track->{release});
    $self->_track_changed($time, $track) if $track_changed;
    my $started = $self->watch->start_at($time);
    if ($started) {
        if ($track_changed || !defined $self->last_track) {
            $self->last_track($track);
            $self->handler->track_started($time, $track);
        } else {
            $self->handler->track_resumed($time, $track);
        }
    }
}

sub pause {
    my ($self, $time, $track) = @_;

    # Now why do we skip if last track is not set? Some players such as Spotify love to throw around pause events
    # when they start up, and they actually contain valid metadata. So for PSM it looks like a legit pause event,
    # just that with an undefined last_track there is nothing we could pause on from our perspective and lots of
    # subsequent checks would be sort-of illegal. As soon as a track is started, last_track will be set, so we do not
    #  miss out on any real pause events.
    $log->info("Ignore pause event") if !defined $self->last_track;
    return if !defined $self->last_track;
    my $track_changed = $self->track->update($track->{artists}, $track->{title}, $track->{release});
    $self->_track_changed($time, $track) if $track_changed;
    my $stopped = $self->watch->stop_at($time);
    if ($stopped) { # note that $stopped implies !$track_changed
        if ($self->last_track->{length_s} - $self->watch->total < $self->gap_to_complete) {
            # This branch covers a nasty corner case of spotify: the player jumps to the first track after it
            # completed the last one and thus *usually* the last track's end event is covered by the track change.
            # However, there are singles with only a single track. Consequently, this track change does not occur at
            # the end of the single. We catch this corner case here by checking if we played it all (except for a gap
            # for tolerance).
            $self->handler->track_ended($time, $self->watch->reset, $self->last_track); # watch reset
            $self->track->clear;
            $self->last_track(undef);
        } else {
            $self->handler->track_paused($time, $self->watch->total, $track);
        }
    }
}

sub stop {pause(@_);}

sub _track_changed {
    my ($self, $time, $track) = @_;

    $self->watch->stop_at($time);
    my $elapsed = $self->watch->reset;
    $log->debug("Elapsed: $elapsed");
    $log->debug("Track length: " . $self->last_track->{length_s});
    $log->debug("Percent played: " . $elapsed / $self->last_track->{length_s} * 100);
    $log->debug("Threshold: " . $self->threshold);
    if ($elapsed / $self->last_track->{length_s} * 100 >= $self->threshold) {
        $self->handler->track_ended($time, $elapsed, $self->last_track);
    } else {
        $self->handler->track_cancelled($time, $elapsed, $self->last_track);
    }
}

1;
