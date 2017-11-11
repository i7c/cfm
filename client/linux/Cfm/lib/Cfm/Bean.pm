package Cfm::Bean;
use strict;
use warnings FATAL => 'all';
require Exporter;
our @ISA = qw(Exporter);
our @EXPORT = qw(bean);

use Cfm::Context;

sub bean {
    my ($name, $sub) = @_;

    Cfm::Context->instance->register_bean($name, $sub);
}

1;