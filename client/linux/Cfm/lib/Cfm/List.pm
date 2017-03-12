package Cfm::List;

use strict;
use Moo;
use Cfm::Resource;

extends 'Cfm::Resource';

my @mandatory = ("pageNumber", "pageSize", "totalElements", "totalPages");

my %mapping = ();

has pageNumber => (is => 'ro');

has pageSize => (is => 'ro');

has totalElements => (is => 'ro');

has totalPages => (is => 'ro');

sub _mandatory_fields {
    return \@mandatory;
}

sub _field_mapping {
    return \%mapping;
}

1;
