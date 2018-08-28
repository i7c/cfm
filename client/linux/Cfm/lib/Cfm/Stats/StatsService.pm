package Cfm::Stats::StatsService;

use strict; use warnings;
use Log::Any qw/$log/;
use Moo;
with 'Cfm::Singleton';
use Cfm::Autowire;

use Cfm::Common::ListRes;
use Cfm::Stats::FirstClassStats;

has client => singleton 'Cfm::Stats::StatsClient';

sub top_release_groups {
    my ($self) = @_;

    $self->client->get_release_groups;
}

sub top_recordings {
    my ($self) = @_;

    $self->client->get_recordings;
}

1;
