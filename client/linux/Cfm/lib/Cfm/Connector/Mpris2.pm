package Cfm::Connector::Mpris2;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
use Log::Any qw/$log/;

use Net::DBus;
use Net::DBus::Reactor;
use Data::Dumper;
use Data::Printer;

use Cfm::Autowire;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackService;
use Cfm::Connector::State::PlayerStateMachine;

has psm => autowire 'Cfm::Connector::State::PlayerStateMachine';
has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub listen {
    my ($self, $dbus_name) = @_;

    $log->debug("Get DBus session");
    my $bus = Net::DBus->session;
    $log->debug("Get DBus service " . $dbus_name);
    my $spotify_service = $bus->get_service("org.mpris.MediaPlayer2.$dbus_name");
    $log->debug("Get DBus interface");
    my $main_interface = $spotify_service->get_object("/org/mpris/MediaPlayer2",
        "org.freedesktop.DBus.Properties");

    my $state = "Playing";

    $log->debug("Hook to signal PropertiesChanged");
    $main_interface->connect_to_signal("PropertiesChanged", sub {
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
                    $log->warn(Dumper($metadata));
                    return;
                }
                for my $artist ($metadata->{"xesam:artist"}->@*) {
                    utf8::decode($artist);
                }
                utf8::decode($metadata->{"xesam:title"});
                utf8::decode($metadata->{"xesam:album"});
                my $data = {
                    artists     => $metadata->{"xesam:artist"},
                    title       => $metadata->{"xesam:title"},
                    album       => $metadata->{"xesam:album"},
                    release     => $metadata->{"xesam:album"},
                    length_ms   => $metadata->{"mpris:length"} / 1000, # to ms
                    length_s    => $metadata->{"mpris:length"} / 1000000, # to s
                    trackNumber => $metadata->{"xesam:trackNumber"},
                    rawdata     => $metadata
                };

                if ($state eq "Playing") {
                    $self->psm->play(time(), $data);
                } elsif ($state eq "Paused") {
                    $self->psm->pause(time(), $data);
                } elsif ($state eq "Stopped") {
                    $self->psm->stop(time(), $data);
                }
            }
            return;
        });
    $log->info("Connection to DBus established. Listening ...");
    my $reactor = Net::DBus::Reactor->main();
    $reactor->run();
}

1;
