package Cfm::ArtistList;

use strict;
use Moo;
use Cfm::Resource;

extends 'Cfm::Resource';

# Mandatory fields
my @mandatory = ("elements", "links");

# Lookup for fields
my %mapping = (
    elements => \&_ds_elements,
    links => \&_ds_links
);

has elements => (
    is => 'ro'
);

sub _ds_elements() {
    my $content = shift;

    my @artists = map {
        Cfm::Artist->from_hash($_)
    } @$content;
    return \@artists;
}

sub _ds_links() {
    my @links = ();
    return \@links;
}

sub _mandatory_fields() {
    return \@mandatory;
}

sub _field_mapping() {
    return \%mapping;
}

1;
