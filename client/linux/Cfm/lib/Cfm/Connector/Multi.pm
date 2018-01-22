package Cfm::Connector::Multi;
use strict;
use warnings FATAL => 'all';
use Log::Any qw/$log/;
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;
use Cfm::Config;
use Cfm::Connector::Mpd;
use Cfm::Connector::Mpris2;
use MCE::Flow;

my %connectors = (
    spotify => sub {
        Cfm::Connector::Mpris2->instance()->listen("spotify");
    },
    mpd     => sub {
        Cfm::Connector::Mpd->instance()->listen();
    },
);

has config => singleton 'Cfm::Config';

sub listen {
    my ($self) = @_;

    my $players_option = $self->config->require_option("players");
    my @players = split /\s*,\s*/, $players_option;
    $log->info("Multi connector for players: " . join(", ", @players));

    my @connectors = map {
        my $player = $_;
        $connectors{$_} // sub {$log->warn("Unknown player $player, ignoring!")}
    } @players;

    mce_flow {max_workers => [ map {1} @connectors ]}, @connectors;
}

1;
