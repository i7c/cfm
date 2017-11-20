package Cfm::Mb::RecordingList;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::List;
use Cfm::Mb::Recording;

extends 'Cfm::List';

# Mandatory fields
my @mandatory = ("elements", "links");

# Mapping functions for fields
my %mapping = (
    elements => \&_ds_elements
);

has elements => (is => 'ro');

sub _ds_elements {
    my ($content) = @_;

    my @recs = map {
        Cfm::Mb::Recording->from_hash($_)
    } @$content;
    return \@recs;
}

sub _mandatory_fields {
    return \@mandatory;
}

sub _field_mapping {
    return \%mapping;
}

1;
