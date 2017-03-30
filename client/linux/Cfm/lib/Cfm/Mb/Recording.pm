package Cfm::Mb::Recording;
use strict;
use warnings FATAL => 'all';
use Moo;

extends 'Cfm::Resource';

my @mandatory = (
    "identifier",
    "name",
    "length",
);

# cfm identifier
has identifier => (is => 'ro');

# Name of the release group
has name => (is => 'ro');

# Length of the song
has length => (is => 'ro');

# Comment
has comment => (is => 'ro');


sub _mandatory_fields {
    return \@mandatory;
}

1;