package Cfm::Client::CfmClient;

use strict;
use warnings;
use Moo;
use Cfm::Autowire;

use Cfm::Config;
use Cfm::Common::ListRes;
use Cfm::Playback::Playback;

with 'Cfm::Singleton';
extends 'Cfm::Client::Client';

has config => singleton 'Cfm::Config';

sub BUILD {
    my ($self, $args) = @_;

    $self->url($self->config->require_option("url"));
    my $user = $self->config->require_option("user");
    my $pass = $self->config->require_option("pass");
    $self->headers->authorization_basic($user, $pass);
}

sub my_playbacks {
    my ($self, $only_broken, $page) = @_;

    my @params = ();
    push @params, onlyBroken => "true" if $only_broken;
    push @params, page => $page if $page > 0;

    Cfm::Common::ListRes->from_hash($self->get_json("/rec/v1/playbacks", \@params),
        sub {
            Cfm::Playback::Playback->from_hash($_);
        });
}

1;
