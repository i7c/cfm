package Cfm::Stats::FirstClassStats;

use strict; use warnings;
use Moo;
use Cfm::Common::Res;
with 'Cfm::Common::Res';

@Cfm::Stats::FirstClassStats::mandatory = (
    'artists',
    'count',
    'id',
    'title',
);

has artists => (is => 'ro');
has count => (is => 'ro');
has id => (is => 'ro');
has title => (is => 'ro');

1;
