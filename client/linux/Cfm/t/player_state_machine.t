#!/usr/bin/perl
use strict;
use warnings;
use Test::More;
use Test::MockObject;

use Cfm::Connector::State::PlayerStateMachine;

sub mock_config {
    my ($config) = @_;

    Test::MockObject->new()
        ->set_true("add_flags")
        ->mock("get_option", sub {
            my ($self, $o) = @_;
            $config->{$o};
        })
        ->mock("has_option", sub {
            my ($self, $o) = @_;

            defined $config->{$o};
        })
        ->mock("require_option", sub {
            my ($self, $o) = @_;

            die unless defined $config->{$o};
            $config->{$o};
        });
}

sub mock_handler() {
    Test::MockObject->new
        ->set_true("track_started")
        ->set_true("track_paused")
        ->set_true("track_resumed")
        ->set_true("track_ended")
        ->set_true("track_cancelled")
}

my $track1 = {
    artists   => [ "A", "B" ],
    title     => [ "t" ],
    release   => [ "r" ],
    length_s  => 10,
    length_ms => 10000,
};

my $track2 = {
    artists   => [ "B" ],
    title     => [ "x" ],
    release   => [ "y" ],
    length_s  => 5,
    length_ms => 5000,
};

my $config = mock_config({
    threshold         => 50,
    'gap-to-complete' => 3,
});

# track change with only play events -> cancel
{
    my $mock_h = mock_handler;
    my $cut = Cfm::Connector::State::PlayerStateMachine->new(
        handler => $mock_h,
        config  => $config,
    );

    $cut->play(1000, $track1);
    $cut->play(1004, $track2);

    $mock_h->called_pos_ok(1, "track_started", "first track started");
    $mock_h->called_pos_ok(2, "track_cancelled", "below threshold is cancel");
}

# track change with only play events -> ended
{
    my $mock_h = mock_handler;
    my $cut = Cfm::Connector::State::PlayerStateMachine->new(
        handler => $mock_h,
        config  => $config,
    );

    $cut->play(1000, $track1);
    $cut->play(1008, $track2);

    $mock_h->called_pos_ok(1, "track_started", "first track started");
    $mock_h->called_pos_ok(2, "track_ended", "above threshold is ended");
}


# small steps (pause/resume) add up to total
{
    my $mock_h = mock_handler;
    my $cut = Cfm::Connector::State::PlayerStateMachine->new(
        handler => $mock_h,
        config  => $config,
    );

    $cut->play(1000, $track1);
    $cut->pause(1001, $track1);
    $cut->play(1001, $track1);
    $cut->pause(1002, $track1);
    $cut->play(1003, $track1);
    $cut->pause(1004, $track1);
    $cut->play(1004, $track1);
    $cut->play(1006, $track2);

    $mock_h->called_pos_ok(1, "track_started", "first track started");
    $mock_h->called_pos_ok(2, "track_paused", "pause");
    $mock_h->called_pos_ok(3, "track_resumed", "resume");
    $mock_h->called_pos_ok(4, "track_paused", "pause");
    $mock_h->called_pos_ok(5, "track_resumed", "resume");
    $mock_h->called_pos_ok(6, "track_paused", "pause");
    $mock_h->called_pos_ok(7, "track_resumed", "resume");
    $mock_h->called_pos_ok(8, "track_ended", "completed because over threshold");
    $mock_h->called_pos_ok(9, "track_started", "new track starts");
}

# silence does not count
{
    my $mock_h = mock_handler;
    my $cut = Cfm::Connector::State::PlayerStateMachine->new(
        handler => $mock_h,
        config  => $config,
    );

    $cut->play(1000, $track1);
    $cut->pause(1001, $track1);
    $cut->play(1009, $track1);
    $cut->play(1010, $track2);

    $mock_h->called_pos_ok(1, "track_started", "first track started");
    $mock_h->called_pos_ok(2, "track_paused", "pause");
    $mock_h->called_pos_ok(3, "track_resumed", "resume");
    $mock_h->called_pos_ok(4, "track_cancelled", "cancelled because elapsed time is low");
    $mock_h->called_pos_ok(5, "track_started", "new track starts");
}

# track change with pause event
{
    my $mock_h = mock_handler;
    my $cut = Cfm::Connector::State::PlayerStateMachine->new(
        handler => $mock_h,
        config  => $config,
    );

    $cut->play(1000, $track1);
    $cut->pause(1001, $track2);

    $mock_h->called_pos_ok(1, "track_started", "first track started");
    $mock_h->called_pos_ok(2, "track_cancelled", "cancelled because below threshold");
}

# pause event at end of last track (thanks spotify)
{
    my $mock_h = mock_handler;
    my $cut = Cfm::Connector::State::PlayerStateMachine->new(
        handler => $mock_h,
        config  => $config,
    );

    $cut->play(1000, $track1);
    $cut->pause(1009, $track1);

    $mock_h->called_pos_ok(1, "track_started", "first track started");
    $mock_h->called_pos_ok(2, "track_ended", "ended because played everything but gap");
}

# ignore bogus signal
{
    my $mock_h = mock_handler;
    my $cut = Cfm::Connector::State::PlayerStateMachine->new(
        handler => $mock_h,
        config  => $config,
    );

    $cut->play(1000, $track1);
    $cut->play(1003, $track1);

    $mock_h->called_pos_ok(1, "track_started", "first track started");
    is ($mock_h->call_pos(2), undef, "no further calls for bogus signal");
}

done_testing;

