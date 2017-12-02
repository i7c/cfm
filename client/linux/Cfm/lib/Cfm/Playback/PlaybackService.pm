package Cfm::Playback::PlaybackService;

use strict;
use warnings;
use Log::Any qw/$log/;
use Moo;
with 'Cfm::Singleton';
use Cfm::Autowire;

use Cfm::Client::CfmClient;

has client => singleton "Cfm::Client::CfmClient";

sub my_playbacks {
    my ($self, $page) = @_;

    $self->client->my_playbacks(0, $page);
}

sub create_playback {
    my ($self, $playback) = @_;

    die $log->error("Not a valid Playback") unless $playback->valid;
    $self->client->create_playback($playback);
}

sub batch_create {
    my ($self, $batch) = @_;

    $self->client->batch_create_playbacks($batch);
}

1;
