package Cfm::Mb::MbsClient;
use strict;
use warnings;
use Moo;
with 'Cfm::Singleton';
extends 'Cfm::Client::Client';

use Cfm::Autowire;
use Cfm::Common::ListRes;
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

# Find recordings by release group id and title
sub find_recordings {
    my ($self, $rgid, $title, $page) = @_;

    my $params = [
        releaseGroupId => $rgid,
        title          => $title,
        page           => $page,
    ];
    my $response = $self->get_json("/mbs/v1/recordings", $params);
}

1;
