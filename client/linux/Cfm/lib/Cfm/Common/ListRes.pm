package Cfm::Common::ListRes;

use strict;
use warnings FATAL => 'all';
use Moo::Role;

use Cfm::Common::Res;
with 'Cfm::Common::Res';

@Cfm::Common::ListRes::mandatory = (
    "elements",
    "size",
    "count",
    "number",
    "total",
    "totalPages",
);

%Cfm::Common::ListRes::mapping = ();

has elements => (is => 'ro');
has size => (is => 'ro');
has count => (is => 'ro');
has number => (is => 'ro');
has total => (is => 'ro');
has totalPages => (is => 'ro');

sub from_hash {
    my ($class, $content, $ds) = @_;

    $class->_check_mandatory_fields($content);
    my %obj = map {
        $_ => $content->{$_};
    } @Cfm::Common::ListRes::mandatory;
    my @elements = map {
        $ds->($_)
    } $content->{"elements"}->@*;
    $obj{"elements"} = \@elements;
    return bless \%obj, $class,
}

1;
