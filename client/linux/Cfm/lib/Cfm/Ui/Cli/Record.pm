package Cfm::Ui::Cli::Record;
use strict; use warnings;
use Moo;
with 'Cfm::Singleton', 'Cfm::Ui::Cli::Command';

use Cfm::Autowire;

has config => singleton 'Cfm::Config';
has loglevel => inject 'loglevel';
has multi => singleton 'Cfm::Connector::Multi';

sub run {
    my ($self) = @_;

    $self->loglevel->("info") unless $self->config->has_option("quiet");
    $self->multi->listen();
}

1;

=head1 NAME

    cfm record - Connect to players and track played songs

=head1 SYNOPSIS

    cfm record [--quiet|-q]

=head1 DESCRIPTION

    Connects to configured players and tracks their state changes to figure out
    which songs are played. This command currently supports mpd and spotify on
    linux. You can "record" other mpris2 players with the record-mpris command.

    record respects the "players" configuration option, which can be set to
    either 'mpd', 'spotify' or a quoted list of both: 'mpd,spotify'. The
    specialty of the record command is, that it uses the "multi connector"
    under the hood, which is capable of tracking all players simultaneously.
    That way, you do not have to spawn multiple instances of cfm to track
    multiple players.

    This command just composes the functionality of the respective record
    commands behind it (i.e. specific connectors). For configuration details of
    the specific commands, please refer to their help instead. All connectors
    should respect the 'threshold' configuration option. It is the percentage
    of the track length, that a song must be played so cfm will consider it a
    playback. It defaults to 50.

=head1 OPTIONS

    --quiet, -q
        Do not blabber all those details about tracked state. With this option
        cfm does not output anything except for errors, as usual.

=cut
