package Cfm::Playback::Playback;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Common::Res;
with "Cfm::Common::Res";

@Cfm::Playback::Playback::mandatory = (
    "artists",
    "recordingTitle",
    "releaseTitle",
    "timestamp",
    "broken",
    "id",
);

%Cfm::Playback::Playback::mapping = (
    broken => \&Cfm::Common::Res::_ds_boolean
);

has artists => (is => 'ro');
has recordingTitle => (is => 'ro');
has releaseTitle => (is => 'ro');
has timestamp => (is => 'ro');
has playTime => (is => 'ro');
has trackLength => (is => 'ro');
has discNumber => (is => 'ro');
has trackNumber => (is => 'ro');
has broken => (is => 'ro');
has id => (is => 'ro');

1;