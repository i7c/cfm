package Cfm::Mb::MbService;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;
use Cfm::Mb::MbsClient;

has client => singleton 'Cfm::Mb::MbsClient';

sub identify_release_group {
    my ($self, $artists, $release, $page) = @_;

    $self->client->get_identified_release_groups($artists, $release, $page);
}

1;
