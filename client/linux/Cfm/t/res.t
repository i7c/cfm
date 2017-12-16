#!/usr/bin/perl
use strict;
use warnings;
use Test::More;
use Test::Exception;

use Cfm::Playback::Playback;

# check mandatory fields fails for missing id
{
    dies_ok {Cfm::Playback::Playback->from_hash(+ {
            artists        => [ "x" ],
            recordingTitle => "x",
            releaseTitle   => "x",
            timestamp      => 1,
            broken         => 1,
        })} "missing id field is not allowed";
}

# check mandatory fields succeeds for missing id if checks are skipped
{
    ok (Cfm::Playback::Playback->from_hash(+ {
                artists        => [ "x" ],
                recordingTitle => "x",
                releaseTitle   => "x",
                timestamp      => 1,
                broken         => 1,
            },
            1), "missing id field is not allowed");
}

# check mandatory fields succeeds
{
    ok (Cfm::Playback::Playback->from_hash(+ {
            artists        => [ "x" ],
            recordingTitle => "x",
            releaseTitle   => "x",
            timestamp      => 1,
            broken         => 1,
            id             => "blubb",
        }), "all fields present succeeds");
}

done_testing();

