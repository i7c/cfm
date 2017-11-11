package Cfm::Singleton;

use strict;
use warnings;
use Moo::Role;

sub instance {
    my ($class) = @_;

    do {
        no strict 'refs';
        ${"${class}::singleton"} //= $class->new;
    }
}

1;