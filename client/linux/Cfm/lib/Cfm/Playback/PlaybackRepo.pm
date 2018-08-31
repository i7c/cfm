package Cfm::Playback::PlaybackRepo;
use strict; use warnings;
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;

has client => singleton "Cfm::Playback::PlaybackClient";

sub find {
  my ($self, $page, $broken) = @_;

  $self->client->my_playbacks($broken, $page);
}

sub store {
  my ($self, $playback) = @_;

  $self->client->create_playback($playback);
}

sub store_all {
  my ($self, $playbacks) = @_;

  $self->client->batch_create_playbacks(
    Cfm::Playback::PlaybackBatchRes->new(playbacks => $playbacks)
  );
}

1;
