package Cfm::Playback::AccumulatedPlaybacks;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Common::Res';

@Cfm::Playback::AccumulatedPlaybacks::mandatory = (
    "occurrences",
    "artistsJson",
    "artists",
    "recordingTitle",
    "releaseTitle",
);

has occurrences => (is => 'ro');
has artistsJson => (is => 'ro');
has artists => (is => 'ro');
has recordingTitle => (is => 'ro');
has releaseTitle => (is => 'ro');
has releaseGroupId => (is => 'rw');
has recordingId => (is => 'rw');

1;
