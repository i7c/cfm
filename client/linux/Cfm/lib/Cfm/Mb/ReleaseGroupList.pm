package Cfm::Mb::ReleaseGroupList;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::List;
use Cfm::Mb::ReleaseGroup;


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

    my @rgs = map {
        Cfm::Mb::ReleaseGroup->from_hash($_)
    } @$content;
    return \@rgs;
}

sub _mandatory_fields {
    return \@mandatory;
}

sub _field_mapping {
    return \%mapping;
}

1;
