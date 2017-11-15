package Cfm::Playback::PlaybackService;

use strict;
use warnings;
use Moo;
with 'Cfm::Singleton';
use Cfm::Autowire;

use Cfm::Client::CfmClient;

has client => singleton "Cfm::Client::CfmClient";

sub my_playbacks {
    my ($self, $page) = @_;

    $self->client->my_playbacks(0, $page);
}

1;
