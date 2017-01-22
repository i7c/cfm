package Cfm::Artist;

use strict;
use Moo;
use Cfm::Resource;

extends 'Cfm::Resource';

my @mandatory = (
    "name",
    "identifier",
    "mbid",
    "links");

# Name of the artist
has name => (
    is => 'ro'
);

# Musicbrainz identifier
has mbid => (
    is => 'ro'
);

# cfm identifier
has identifier => (
    is => 'ro'
);

sub from_hash() {
    my $class = shift;
    my $content = shift;

    Cfm::Artist->_check_mandatory_fields($content);

    return bless $content, $class;
}


sub _mandatory_fields() {
    return \@mandatory;
}

1;
