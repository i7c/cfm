package Cfm::Playback;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Resource;
use Cfm::Recording;

extends "Cfm::Resource";

my @mandatory = (
    "recording",
    "time",
    "userRef"
);

my %mapping = (
    recording => \&_ds_recording
);

has time => (is => 'ro');

has recording => (is => 'ro');

sub _ds_recording {
    my ($content) = @_;

    return Cfm::Recording->from_hash($content);
}

sub _field_mapping {
    return \%mapping;
}

sub _mandatory_fields {
    return \@mandatory;
}

1;