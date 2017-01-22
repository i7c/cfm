package Cfm::Resource;

use strict;
use Moo;

my @mandatory = ();
my %mapping = ();

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


# Creates new object from hash
sub from_hash() {
    my $class = shift;
    my $content = shift;

    $class->_check_mandatory_fields($content);
    my $obj = $class->deserialise($content);
    return bless $content, $class;
}

# Deserialise hash into objects using the mapping returned by _field_mapping()
sub deserialise() {
    my ($class, $content) = @_;

    my %obj = map {
        $_ => $class->_deserialise_field($_, $content->{$_})
    } keys %$content;
    return \%obj;
}

# Deserialise single field using the mapping
sub _deserialise_field() {
    my ($class, $key, $value) = @_;

    my $mapping = $class->_field_mapping;
    if ($mapping->{$key}) {
        return $mapping->{$key}->($value);
    } else {
        return $value;
    }
}

# Returns mandatory fields for this type
sub _mandatory_fields() {
    return \@mandatory;
}

# Returns field mapping for this type
sub _field_mapping() {
    return \%mapping;
}

1;
