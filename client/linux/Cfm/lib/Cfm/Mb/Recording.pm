package Cfm::Mb::Recording;
use strict;
use warnings FATAL => 'all';
use Moo;
use Cfm::Common::Res;
with 'Cfm::Common::Res';

@Cfm::Mb::Recording::mandatory = (
    "id",
    "name",
    "length",
    "artists",
);

has id => (is => 'ro');
has name => (is => 'ro');
has length => (is => 'ro');
has artists => (is => 'ro');

1;
