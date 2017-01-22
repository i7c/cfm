package Cfm::Resource;

use strict;
use Moo;

my @mandatory = ();

# Checks if all mandatory fields are present
sub _check_mandatory_fields {
    my ($class, $content) = @_;
    my $fields = $class->_mandatory_fields();

    for my $field (@$fields) {
        if (! $content->{$field}) {
            die "Missing field $field for $class\n";
        }
    }
}

# Returns mandatory fields for this type
sub _mandatory_fields() {
    return \@mandatory;
}

1;
