package Cfm::Mb::MbsClient;
use strict;
use warnings;
use Moo;
with 'Cfm::Singleton';
extends 'Cfm::Client::Client';

use Cfm::Autowire;
use Cfm::Common::ListRes;
use Cfm::Mb::Recording;
use Cfm::Mb::ReleaseGroup;

has config => singleton 'Cfm::Config';

sub BUILD {
    my ($self, $args) = @_;

    $self->url($self->config->require_option("url"));
    my $user = $self->config->require_option("user");
    my $pass = $self->config->require_option("pass");
    $self->headers->authorization_basic($user, $pass);
}

sub get_identified_release_groups {
    my ($self, $artists, $release, $page) = @_;

    my @params = ();
    map {
        push @params, artist => $_;
    } $artists->@*;

    push @params, release => $release;
    push @params, page => $page if defined $page;

    Cfm::Common::ListRes->from_hash(
        $self->get_json("/mbs/v1/releasegroups/identify", \@params), sub {Cfm::Mb::ReleaseGroup->from_hash($_)}
    );
}

sub get_identified_recordings {
    my ($self, $rgid, $title, $page) = @_;

    my @params;
    push @params, releaseGroupId => $rgid;
    push @params, title => $title;
    push @params, page => $page if defined $page;
    Cfm::Common::ListRes->from_hash(
        $self->get_json("/mbs/v1/recordings/identify", \@params), sub {Cfm::Mb::Recording->from_hash($_)}
    );
}

1;
