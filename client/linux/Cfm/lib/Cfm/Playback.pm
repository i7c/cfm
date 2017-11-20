package Cfm::Playback;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Resource;
use Cfm::Recording;
use Cfm::ReleaseGroup;

extends "Cfm::Resource";

my @mandatory = (
    "time",
    "userRef",
    "identifier"
);

my %mapping = (
    recording => \&_ds_recording,
    releaseGroup => \&_ds_release_group
);

has recording => (is => 'ro');

has releaseGroup => (is => 'ro');

has time => (is => 'ro');

has identifier => (is => 'ro');

has originalTitle => (is => 'ro');

has originalAlbum => (is => 'ro');

has originalArtists => (is => 'ro');

sub _ds_recording {
    my ($content) = @_;

    return Cfm::Recording->from_hash($content);
}

sub _ds_release_group {
    my ($content) = @_;

    return Cfm::ReleaseGroup->from_hash($content);
}

sub _field_mapping {
    return \%mapping;
}

sub _mandatory_fields {
    return \@mandatory;
}

1;
