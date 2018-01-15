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

# mark_unfixable()
{
    my $mock_c = Test::MockObject->new()
        ->set_true("post_accumulated_playbacks");

    my $acc = Cfm::Playback::AccumulatedPlaybacks->new(
        occurrences    => 5,
        artistsJson    => '["x"]',
        artists        => [ "x" ],
        recordingTitle => "y",
        releaseTitle   => "z",
        releaseGroupId => "blabla",
        recordingId    => "blublu",
    );
    cut(client => $mock_c)->mark_unfixable($acc);

    $mock_c->called_pos_ok(1, "post_accumulated_playbacks", "calls post_accumulated_playbacks on client");
    my $actualAcc = $mock_c->call_args_pos(1, 2);
    is ($actualAcc->occurrences, 5, "occurrences unchanged");
    is ($actualAcc->artistsJson, '["x"]', "artistsJson unchanged");
    is ($actualAcc->releaseGroupId, undef, "releaseGroupId set to undef");
    is ($actualAcc->recordingId, undef, "recordingId set to undef");
}

done_testing();
