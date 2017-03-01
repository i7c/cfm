package Cfm::ReleaseGroup;
use strict;
use Moo;
use Cfm::Resource;

extends 'Cfm::Resource';

my @mandatory = (
    "title"
);

# Title of the release group
has title => (is => 'ro');

# Musicbrainz identifier
has mbid => (is => 'ro');

# cfm identifier
has identifier => (is => 'ro');


sub _mandatory_fields {
    return \@mandatory;
}

1;
