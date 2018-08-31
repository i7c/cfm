package Cfm::Playback::PlaybackService;

use strict; use warnings;
use Log::Any qw/$log/;
use Moo;
with 'Cfm::Singleton';
use Cfm::Autowire;

has client => singleton "Cfm::Playback::PlaybackClient";
has repo => singleton 'Cfm::Playback::PlaybackRepo';

sub my_playbacks {
    my ($self, $page, $broken) = @_;

    $self->repo->find($page // 0, $broken // 0);
}

sub create_playback {
    my ($self, $playback) = @_;

    die $log->error("Not a valid Playback") unless $playback->valid;
    $self->repo->store($playback);
}

sub batch_create {
    my ($self, $playbacks) = @_;

    $self->repo->store_all($playbacks);
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

sub delete {
    my ($self, $source) = @_;

    $self->client->delete_playbacks($source);
}

sub fixlog {
    my ($self, $page) = @_;

    $self->client->get_fixlog($page);
}

1;
