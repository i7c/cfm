package Cfm::User::UserService;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
use Log::Any qw/$log/;

use Cfm::Autowire;

has client => singleton "Cfm::Client::CfmClient";

sub create_user {
    my ($self, $user) = @_;

    die $log->error("Not a valid User") unless $user->valid;
    $self->client->post_user($user);
}

1;
