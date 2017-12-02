package Cfm::Playback::BatchResultRes;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Common::Res;
with "Cfm::Common::Res";

use Cfm::Playback::BatchResultItemRes;

%Cfm::Playback::BatchResultRes::mapping = (
    results => sub {
        [ map {
            Cfm::Playback::BatchResultItemRes->from_hash($_);
        } $_[0]->@* ];
    },
);

has results => (is => 'ro');

1;
