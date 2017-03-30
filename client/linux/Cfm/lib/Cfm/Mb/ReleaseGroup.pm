package Cfm::Mb::ReleaseGroup;
use strict;
use Moo;
use Cfm::Resource;

extends 'Cfm::Resource';

my @mandatory = (
    "name",
    "identifier",
);

# Name of the release group
has name => (is => 'ro');

# Comment
has comment => (is => 'ro');

# cfm identifier
has identifier => (is => 'ro');


sub _mandatory_fields {
    return \@mandatory;
}

1;
