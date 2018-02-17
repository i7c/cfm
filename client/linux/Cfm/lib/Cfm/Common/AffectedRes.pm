package Cfm::Common::AffectedRes;
use strict;
use warnings FATAL => 'all';
use Moo::Role;
with 'Cfm::Common::Res';

has affected => (is => 'ro');

1;
