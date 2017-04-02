package Cfm::Dto::Reference;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Resource;

extends 'Cfm::Resource';

my @mandatory = (
    "displayName",
    "identifier",
);

# Name to Display
has displayName => (is => 'ro');

# Identifier of the reference target
has identifier => (is => 'ro');

sub _mandatory_fields {
    return \@mandatory;
}

1;
