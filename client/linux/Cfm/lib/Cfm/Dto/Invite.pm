package Cfm::Dto::Invite;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Resource;
use Cfm::Dto::Reference;

extends 'Cfm::Resource';

sub _mandatory_fields {
    return [
        "identifier",
        "date",
    ]
}

has identifier => (is => 'ro');

has date => (is => 'ro');

1;