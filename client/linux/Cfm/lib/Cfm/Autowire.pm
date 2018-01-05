package Cfm::Autowire;

use strict;
use warnings;
require Exporter;
our @ISA = qw(Exporter);
our @EXPORT = qw(autowire singleton inject);

sub autowire {
    my ($class, $does, @args) = @_;
    return (
        is      => 'lazy',
        default => sub {$class->new(@args)},
        isa     => sub {$_[0]->DOES($does // $class)}
    );
}

sub singleton {
    my ($class, $does) = @_;
    return (
        is      => 'lazy',
        default => sub {$class->instance},
        isa     => sub {$_[0]->DOES($does // $class)}
    );
}

sub inject {
    my ($name) = @_;

    return (
        is      => 'lazy',
        default => sub {
            use Cfm::Context;
            Cfm::Context->instance->get_bean($name);
        }
    );
}

1;
