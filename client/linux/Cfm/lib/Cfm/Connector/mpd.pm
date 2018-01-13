package Cfm::Connector::Mpd;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
use Log::Any qw/$log/;

use MCE::Flow;
use MCE::Queue;
use Data::Dumper;
use Net::MPD;
use Time::HiRes qw/time/;
use Try::Tiny;

use Cfm::Autowire;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackService;
use Cfm::Connector::State::PlayerStateMachine;

has queue => (
        is      => 'ro',
        default => sub {MCE::Queue->new}
    );

has psm => autowire 'Cfm::Connector::State::PlayerStateMachine';
has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub listen {
    my ($self) = @_;

    mce_flow {max_workers => [ 1, 1 ]},
        sub {
            $self->_mpd_connect();
        },
        sub {
            while (defined (my $data = $self->queue->dequeue)) {
                if ($data->{state} eq "play") {
                    $self->psm->play($data->{timestamp}, $data);
                } elsif ($data->{state} eq "pause") {
                    $self->psm->pause($data->{timestamp}, $data);
                } elsif ($data->{state} eq "stop") {
                    $self->psm->stop($data->{timestamp}, $data);
                }
            }
        };
}

sub _mpd_connect {
    my $mpd;

    # make sure the connection is alive
    $mpd //= Net::MPD->connect($ENV{MPD_HOST} // 'localhost');             
    try {
        $mpd->ping;
    } catch {
        $mpd->_connect;
    };

    while() {
        my @mpd_currentsong;
        my $mpd_status;
    
        my $state;
        my ($duration, $duration_ms, $duration_s);
    
        $log->debug("Waiting for event from mpd");
        $mpd->idle->{'player'}; # blocking until mpd sends an update
        $mpd_status = $mpd->update_status();
        $mpd_currentsong = $mpd->current_song();
        $state = $mpd_status->{state};
        push @mpd_currentsong, $state;
    
        $duration=$mpd_currentsong[1]->{duration};
        $duration_s = sprintf "%.0f", $duration;
        my @track_time = split /\./, $duration;
        $duration_ms = $track_time[0]*1000+$track_time[1];
    
        if(!defined $mpd_currentsong->{"Title"}
            || !defined $mpd_currentsong->{"duration"}
            || $mpd_currentsong->{"Title"} eq ""
            || $mpd_currentsong->{"duration"} == 0 {
                $log->warn("Ignoring bogus signal from mpd.");
                $log->debug(Dumper($mpd_currentsong));
                return;
            }

    
        my $data = {
            artists                     => $mpd_currentsong->{"Artist"},
            title                       => $mpd_currentsong->{"Title"},
            album                       => $mpd_currentsong->{"Album"},
            release                     => $mpd_currentsong->{"Album"},
            length_ms                   => $mpd_currentsong->{"duration_ms"},
            length_s                    => $mpd_currentsong->{"duration_s"},
            trackNumber                 => $mpd_currentsong->{"Track"},
            MUSICBRAINZ_ALBUMARTISTID   => $mpc_currentsong->{"MUSICBRAINZ_ALBUMARTISTID"},
            MUSICBRAINZ_ALBUMID         => $mpc_currentsong->{"MUSICBRAINZ_ALBUMID"},
            MUSICBRAINZ_RELEASETRACKID  => $mpc_currentsong->{"MUSICBRAINZ_RELEASETRACKID"},
            MUSICBRAINZ_TRACKID         => $mpc_currentsong->{"MUSICBRAINZ_TRACKID"},
            MUSICBRAINZ_ARTISTID        => $mpc_currentsong->{"MUSICBRAINZ_ARTISTID"},
            rawdata                     => $mpd_currentsong,
            state                       => $state,
            timestamp                   => time(),
        };
    
        $self->queue->enqueue($data);
        return;
    }
}

1;
