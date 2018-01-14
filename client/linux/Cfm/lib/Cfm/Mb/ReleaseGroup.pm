package Cfm::Mb::ReleaseGroup;
use strict;
use Moo;
with 'Cfm::Common::Res';

@Cfm::Mb::ReleaseGroup::mandatory = (
    "id",
    "name",
    "artists",
);

has id => (is => 'ro');
has name => (is => 'ro');
has artists => (is => 'ro');

1;
