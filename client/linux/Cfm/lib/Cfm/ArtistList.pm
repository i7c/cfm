package Cfm::ArtistList;

use strict;
use Moo;
use Cfm::Resource;

extends 'Cfm::Resource';

# Mandatory fields
my @mandatory = ("elements", "links");

# Mapping functions for fields
my %mapping = (
    elements => \&_ds_elements,
    links    => \&_ds_links
);

has elements => (is => 'ro');

# deserialise elements
sub _ds_elements {
    my ($content) = @_;

    my @artists = map {
        Cfm::Artist->from_hash($_)
    } @$content;
    return \@artists;
}

# deserialise links
sub _ds_links {
    my @links = ();
    return \@links;
}

sub _mandatory_fields {
    return \@mandatory;
}

sub _field_mapping {
    return \%mapping;
}

1;
