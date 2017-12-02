package Cfm::Playback::BatchResultItemRes;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Common::Res;
with "Cfm::Common::Res";

%Cfm::Playback::BatchResultItemRes::mapping = (
    success => \&Cfm::Common::Res::_ds_boolean,
);

has success => (is => 'ro');

1;
