package Cfm::Connector::Mpris2;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
use Log::Any qw/$log/;

use Data::Dumper;
use MCE::Flow;
use MCE::Queue;
use Net::DBus::Dumper;
use Net::DBus::Reactor;
use Net::DBus;
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

has config => autowire 'Cfm::Config';
has psm => autowire 'Cfm::Connector::State::PlayerStateMachine';
has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub listen {
    my ($self, $dbus_name) = @_;

    mce_flow {max_workers => [ 1, 1 ]},
        sub {
            $self->_mpris_connect($dbus_name);
        },
        sub {
            while (defined (my $data = $self->queue->dequeue)) {
                if ($data->{state} eq "Playing") {
                    $self->psm->play($data->{timestamp}, $data);
                } elsif ($data->{state} eq "Paused") {
                    $self->psm->pause($data->{timestamp}, $data);
                } elsif ($data->{state} eq "Stopped") {
                    $self->psm->stop($data->{timestamp}, $data);
                }
            }
        };
}

sub _mpris_connect {
    my ($self, $dbus_name) = @_;

    my $wait = $self->config->require_option("mpris-wait");
    my $bus = Net::DBus->session;
    $log->info("Getting DBus service $dbus_name ...");
    while () {
        try {
            $log->debug("Try to get DBus service $dbus_name");
            my $service = $bus->get_service("org.mpris.MediaPlayer2.$dbus_name");
            my $player = $service->get_object("/org/mpris/MediaPlayer2", "org.mpris.MediaPlayer2.Player");
            my $event = $service->get_object("/org/mpris/MediaPlayer2", "org.freedesktop.DBus.Properties");
            $self->_mpris_listen($player, $event);
        } catch {
            $log->debug("DBus Service $dbus_name not found, will sleep for ${wait}s ...");
            sleep($wait);
        };
    }
}

sub _mpris_listen {
    my ($self, $player, $event) = @_;

    my $metadata = $player->Metadata();
    my $state = $player->PlaybackStatus();
    $log->info("Set initial state and track metadata");
    $self->queue->enqueue($self->_extract_metadata($metadata, $state));

    $log->debug("Hook to signal PropertiesChanged");
    $event->connect_to_signal("PropertiesChanged", sub {
            my ($player, $rawdata) = @_;

            $log->debug(Dumper(@_));

            if (defined $rawdata->{PlaybackStatus}) {
                $log->debug("Received playback status update: " . $rawdata->{PlaybackStatus});
                $state = $rawdata->{PlaybackStatus};
            }
            if (defined $rawdata->{Metadata}) {
                my $metadata = $rawdata->{Metadata};
                if (!defined $metadata->{"xesam:title"}
                    || !defined $metadata->{"mpris:length"}
                    || $metadata->{"xesam:title"} eq ""
                    || $metadata->{"mpris:length"} == 0) {
                    $log->warn("Ignoring bogus signal from mpris2.");
                    $log->debug(Dumper($metadata));
                    return;
                }
                $self->queue->enqueue($self->_extract_metadata($metadata, $state));
            }
        });
    $log->info("Connection to DBus established. Listening ...");
    my $reactor = Net::DBus::Reactor->main();
    $reactor->run();
}

sub _extract_metadata {
    my ($self, $metadata, $state) = @_;

    for my $artist ($metadata->{"xesam:artist"}->@*) {
        utf8::decode($artist);
    }
    utf8::decode($metadata->{"xesam:title"});
    utf8::decode($metadata->{"xesam:album"});
    + {
        artists     => $metadata->{"xesam:artist"},
        title       => $metadata->{"xesam:title"},
        album       => $metadata->{"xesam:album"},
        release     => $metadata->{"xesam:album"},
        length_ms   => $metadata->{"mpris:length"} / 1000, # to ms
        length_s    => $metadata->{"mpris:length"} / 1000000, # to s
        trackNumber => $metadata->{"xesam:trackNumber"},
        rawdata     => $metadata,
        state       => $state,
        timestamp   => time(),
    };
}

1;
