#!/usr/bin/perl
use strict;
use warnings;
use Test::More;
use Test::Exception;
use Test::MockObject;

use Cfm::Playback::AccumulatedPlaybacks;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackService;

sub cut {Cfm::Playback::PlaybackService->new(@_)}

# create_playback
{
    my $playback = Cfm::Playback::Playback->new;
    dies_ok {cut()->create_playback($playback)} "invalid playback should die";
}

done_testing();
