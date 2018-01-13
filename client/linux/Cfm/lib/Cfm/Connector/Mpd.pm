package Cfm::Connector::Mpd;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
use Log::Any qw/$log/;

use Cfm::Autowire;
use Cfm::Config;
use Cfm::Connector::State::PlayerStateMachine;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackService;
use Data::Dumper;
use MCE::Flow;
use MCE::Queue;
use Net::MPD;
use Time::HiRes qw/time/;
use Try::Tiny;

has config => singleton 'Cfm::Config';
has playback_service => singleton 'Cfm::Playback::PlaybackService';
has psm => autowire 'Cfm::Connector::State::PlayerStateMachine';
has queue => (is => 'ro', default => sub {MCE::Queue->new});

sub listen {
    my ($self) = @_;

    mce_flow {max_workers => [ 1, 1 ]},
        sub {
            $self->_mpd_connect();
        },
        sub {
            while (defined (my $data = $self->queue->dequeue)) {
                if ($data->{state} eq "play") {
                    $self->psm->play($data->{timestamp}, $data->{metadata});
                } elsif ($data->{state} eq "pause") {
                    $self->psm->pause($data->{timestamp}, $data->{metadata});
                } elsif ($data->{state} eq "stop") {
                    $self->psm->stop($data->{timestamp});
                }
            }
        };
}

sub _mpd_connect {
    my ($self) = @_;

    my $wait = $self->config->require_option("mpd-wait");
    $log->info("Connecting to MPD at " . $self->config->require_option("mpd-host"));
    while () {

        try {
            my $mpd = Net::MPD->connect($self->config->require_option("mpd-host"));
            $log->info("Connection to MPD established");
            $self->_mpd_listen($mpd);
        } catch {
            $log->debug("Could not connect to MPD, will sleep for ${wait}s ...");
            sleep($wait);
        };
    }
}


sub _mpd_listen {
    my ($self, $mpd) = @_;

    use Data::Dumper;
    my $metadata = $mpd->current_song();
    my $state = $mpd->update_status();

    $log->info("Set initial state and track metadata");
    $self->queue->enqueue($self->_extract_metadata($metadata, $state));

    $log->info("Waiting for events on mpd ...");
    while () {
        $log->debug("Waiting for event from mpd");
        $mpd->idle('player'); # blocking until mpd sends an update

        $state = $mpd->update_status();
        $metadata = $mpd->current_song();
        $self->queue->enqueue($self->_extract_metadata($metadata, $state));
    }
}

sub _is_bogus_signal {
    my ($self, $metadata) = @_;

    if (!$metadata
        || !$metadata->{"Title"}
        || !$metadata->{"duration"}
        || !$metadata->{"Artist"}
        || !$metadata->{"Album"}
        || $metadata->{"duration"} == 0) {
        $log->debug("Bogus signal from mpd");
        return 1;
    }
    0;
}

sub _extract_metadata {
    my ($self, $metadata, $state) = @_;

    $log->debug(Dumper($metadata));
    $log->debug(Dumper($state));
    my $data = {
        state     => $state->{state},
        timestamp => time(),
    };
    $data->{metadata} = {
        artists             => [ $metadata->{"Artist"} ],
        title               => $metadata->{"Title"},
        album               => $metadata->{"Album"},
        release             => $metadata->{"Album"},
        length_ms           => $metadata->{"duration"} * 1000,
        length_s            => $metadata->{"duration"},
        trackNumber         => $metadata->{"Track"},
        mb_album_artist_id  => $metadata->{"MUSICBRAINZ_ALBUMARTISTID"},
        mb_album_id         => $metadata->{"MUSICBRAINZ_ALBUMID"},
        mb_release_track_id => $metadata->{"MUSICBRAINZ_RELEASETRACKID"},
        mb_track_id         => $metadata->{"MUSICBRAINZ_TRACKID"},
        mb_artist_id        => $metadata->{"MUSICBRAINZ_ARTISTID"},
        rawdata             => $metadata,
    } unless $self->_is_bogus_signal($metadata);
    $data;
}

1;
