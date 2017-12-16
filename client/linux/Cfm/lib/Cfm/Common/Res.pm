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
    my ($class, $content, $skip_check) = @_;

    $class->_check_mandatory_fields($content) unless $skip_check;
    my $obj = $class->deserialise($content);
    return bless $obj, $class;
}

sub to_hash {
    my ($class, $obj) = @_;

    my $field_projections = $class->_field_projection();
    my %projection = ();
    for my $key (keys $obj->%*) {
        next if !defined $obj->{$key};
        if (defined $field_projections->{$key}) {
            $projection{$key} = $field_projections->{$key}->($obj->{$key});
        } else {
            $projection{$key} = $obj->{$key};
        }
    }
    return \%projection;
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

sub _field_projection {
    my ($class) = @_;
    do {
        no strict 'refs';
        \%{"${class}::projection"}
    }
}

sub _ds_boolean {
    $_[0] + 0
}

1;
