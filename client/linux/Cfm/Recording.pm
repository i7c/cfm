package Cfm::Recording;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Resource;
use Cfm::Artist;

extends 'Cfm::Resource';

my %mapping = (
    artists => \&_ds_artists
);

my @mandatory = ("identifier", "artists", "title");

# cfm identifier
has identifier => (is => 'ro');

# artist list
has artists => (is => 'ro');

# title
has title => (is => 'ro');

# mbid
has mbid => (is => 'ro');

# Deserialise artists section
sub _ds_artists {
    my ($content) = @_;

    my @artists = map {
        Cfm::Artist->from_hash($_);
    } @$content;
    return \@artists;
}

sub _field_mapping {
    return \%mapping;
}

sub _mandatory_fields {
    return \@mandatory;
}

1;