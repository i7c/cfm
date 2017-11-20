package Cfm::Mb::Recording;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Resource;
use Cfm::Dto::Reference;

extends 'Cfm::Resource';

my %mapping = (
    artistReferences => \&_ds_artist_references,
);

my @mandatory = (
    "identifier",
    "name",
    "length",
    "artistReferences",
);

# cfm identifier
has identifier => (is => 'ro');

# Name of the release group
has name => (is => 'ro');

# Length of the song
has length => (is => 'ro');

# Comment
has comment => (is => 'ro');

# Artist references
has artistReferences => (is => 'ro');

sub _ds_artist_references {
    my ($content) = @_;

    my @arefs = map {
        Cfm::Dto::Reference->from_hash($_);
    } @$content;
    return \@arefs;
}

sub _field_mapping {
    return \%mapping;
}

sub _mandatory_fields {
    return \@mandatory;
}

1;
