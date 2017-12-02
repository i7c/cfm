package Cfm::Playback::PlaybackBatchRes;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Common::Res;
with "Cfm::Common::Res";

use Cfm::Playback::Playback;

%Cfm::Playback::PlaybackBatchRes::projection = (
    playbacks => sub {
        [ map {
            Cfm::Playback::Playback->to_hash($_)
        } $_[0]->@* ];
    }
);

has playbacks => (is => 'ro');

1;
