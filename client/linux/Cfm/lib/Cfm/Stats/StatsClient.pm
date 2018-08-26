package Cfm::Stats::StatsClient;

use strict; use warnings;
use Moo;
use Cfm::Autowire;

with 'Cfm::Singleton';
extends 'Cfm::Client::Client';

has config => singleton 'Cfm::Config';

sub BUILD {
    my ($self) = @_;

    $self->init(
        $self->config->require_option("url"),
        "/rec/v1/stats",
        $self->config->require_option("user"),
        $self->config->require_option("pass"),
    );
}

sub get_release_groups {
    my ($self) = @_;

    Cfm::Common::ListRes->from_hash(
        $self->get_json("/release_groups"),
        sub { Cfm::Stats::FirstClassStats->from_hash($_) },
    );
}

sub get_recordings {
    my ($self) = @_;

    Cfm::Common::ListRes->from_hash(
        $self->get_json("/recordings"),
        sub { Cfm::Stats::FirstClassStats->from_hash($_) },
    );
}

1;
