package Cfm::Common::Res;

use strict;
use warnings;
use Moo::Role;

sub _check_mandatory_fields {
    my ($class, $content) = @_;
    my $fields = $class->_mandatory_fields;

    for my $field (@$fields) {
        die "Missing field $field for $class\n" unless defined $content->{$field};
    }
}

sub from_hash {
    my ($class, $content) = @_;

    $class->_check_mandatory_fields($content);
    my $obj = $class->deserialise($content);
    return bless $obj, $class;
}

sub deserialise {
    my ($class, $content) = @_;

    my %obj = map {
        $_ => $class->_deserialise_field($_, $content->{$_})
    } keys %$content;
    return \%obj;
}

sub _deserialise_field {
    my ($class, $key, $value) = @_;

    my $mapping = $class->_field_mapping;
    if ($mapping->{$key}) {
        return $mapping->{$key}->($value);
    } else {
        return $value;
    }
}

sub _mandatory_fields {
    my ($class) = @_;

    do {
        no strict 'refs';
        \@{"${class}::mandatory"}
    }
}

sub _field_mapping {
    my ($class) = @_;
    do {
        no strict 'refs';
        \%{"${class}::mapping"}
    }
}

sub _ds_boolean {
    $_[0] + 0
}

1;
