package Cfm::PlaybackList;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Resource;
use Cfm::Playback;

extends 'Cfm::Resource';

# Mandatory fields
my @mandatory = ("elements", "links");

# Mapping functions for fields
my %mapping = (
    elements => \&_ds_elements
);

has elements => (is => 'ro');

sub _ds_elements {
    my ($content) = @_;

    my @playbacks = map {
        Cfm::Playback->from_hash($_)
    } @$content;
    return \@playbacks;
}

sub _mandatory_fields {
    return \@mandatory;
}

sub _field_mapping {
    return \%mapping;
}

1;