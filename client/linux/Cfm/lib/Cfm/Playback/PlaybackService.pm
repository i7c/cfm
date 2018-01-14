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
    my ($self, $page, $broken) = @_;

    $self->client->my_playbacks($broken // 0, $page);
}

sub accumulated_broken_playbacks {
    my ($self, $page) = @_;

    $self->client->get_accumulated_playbacks($page);
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

sub set_now_playing {
    my ($self, $playback) = @_;

    $log->info("Set now playing ...");
    $self->client->put_now_playing($playback)
}

sub get_now_playing {
    my ($self) = @_;

    $self->client->get_now_playing;
}

sub fix_acc_playback {
    my ($self, $acc) = @_;

    $self->client->post_accumulated_playbacks($acc);
}

1;
