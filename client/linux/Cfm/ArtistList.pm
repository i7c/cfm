package Cfm::ArtistList;

use strict;
use Moo;
use Data::Dumper;
use Cfm::Resource;

extends 'Cfm::Resource';

# Mandatory fields
my @mandatory = ("elements", "links");

# Lookup for fields
my %dslookup = (
    elements => \&_ds_elements,
    links => \&_ds_links
);


has elements => (
    is => 'ro'
);


# Deserialisation Mechanics

sub from_hash() {
    my ($class, $content) = @_;

    Cfm::ArtistList->_check_mandatory_fields($content);

    my %obj = map {
        $_ => _deserialise($_, $content->{$_})
    } keys %$content;

    return bless \%obj, $class;
}

sub _deserialise() {
    my ($key, $value) = @_;

    if ($dslookup{$key}) {
        return $dslookup{$key}->($value);
    } else {
        return $value;
    }
}

sub _ds_elements() {
    my $content = shift;

    my @artists = map {
        Cfm::Artist->from_hash($_)
    } @$content;
    return \@artists;
}

sub _ds_links() {
    my @links = ();
    return \@links;
}

sub _mandatory_fields() {
    return \@mandatory;
}

1;
