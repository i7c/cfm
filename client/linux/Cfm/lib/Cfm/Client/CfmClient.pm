package Cfm::Client::CfmClient;

use strict;
use warnings;
use Moo;
use Cfm::Autowire;

use Cfm::Common::AffectedRes;
use Cfm::Common::ListRes;
use Cfm::Playback::AccumulatedPlaybacks;
use Cfm::Playback::BatchResultRes;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackBatchRes;

with 'Cfm::Singleton';
extends 'Cfm::Client::Client';

has config => singleton 'Cfm::Config';

sub BUILD {
    my ($self, $args) = @_;

    $self->init(
        $self->config->require_option("url"),
        $self->config->require_option("user"),
        $self->config->require_option("pass"),
    );
}

sub post_user {
    my ($self, $user) = @_;

    Cfm::User::User->from_hash(
        $self->post_json("/rec/v1/users", Cfm::User::User->to_hash($user))
    );
}

1;
