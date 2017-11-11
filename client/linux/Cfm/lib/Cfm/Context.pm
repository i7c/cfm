package Cfm::Context;
use strict;
use warnings FATAL => 'all';
use Moo;

with 'Cfm::Singleton';

my %bean_cache = ();

my %bean_builder = ();

sub get_bean {
    my ($self, $name) = @_;

    $bean_cache{$name} //= $bean_builder{$name}->();
}

sub register_bean {
    my ($self, $name, $builder) = @_;

    $bean_builder{$name} = $builder;
}

1;