package Cfm::Client::MbsClient;

use Moo;
use strict;
use warnings;

use Cfm::Mb::ReleaseGroupList;
use Cfm::Mb::RecordingList;

extends 'Cfm::Client::Client';

# Find release groups by artist and release name, ordered by rating
sub find_releasegroups {
    my ($self, $artists, $release, $page) = @_;

    my @params = ();
    for my $artist ($artists->@*) {
        push @params, "artist";
        push @params, $artist;
    }
    push @params, "release";
    push @params, $release;
    if (defined $page) {
        push @params, "page";
        push @params, $page;
    }
    my $response = $self->get_json("/mbs/v1/releasegroups", \@params);
    return Cfm::Mb::ReleaseGroupList->from_hash($response);
}

# Find recordings by release group id and title
sub find_recordings {
    my ($self, $rgid, $title, $page) = @_;

    my $params = [
        "releaseGroupId" => $rgid,
        "title"          => $title,
        "page"           => $page,
    ];
    my $response = $self->get_json("/mbs/v1/recordings", $params);
    return Cfm::Mb::RecordingList->from_hash($response);
}

1;
