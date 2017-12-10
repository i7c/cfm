use strict;
use warnings;
use Test::More;

use Cfm::Connector::State::TrackState;

# initial
{
    my $cut = Cfm::Connector::State::TrackState->new;

    my $changed = $cut->update(
        [ "a1", "a2" ],
        "title1",
        "release",
    );

    is ($changed, 0, "set initial track is not a change");
}

# same data twice
{
    my $cut = Cfm::Connector::State::TrackState->new;

    $cut->update(
        [ "a1", "a2" ],
        "title1",
        "release",
    );
    my $changed = $cut->update(
        [ "a1", "a2" ],
        "title1",
        "release",
    );

    is ($changed, 0, "same data is no change");
}

# album change
{
    my $cut = Cfm::Connector::State::TrackState->new;

    $cut->update(
        [ "a1", "a2" ],
        "title1",
        "release",
    );
    my $changed = $cut->update(
        [ "a1", "a2" ],
        "title1",
        "releas",
    );

    is ($changed, 1, "album change");
}

# artist change
{
    my $cut = Cfm::Connector::State::TrackState->new;

    $cut->update(
        [ "a1" ],
        "title1",
        "release",
    );
    my $changed = $cut->update(
        [ "a1", "a2" ],
        "title1",
        "release",
    );

    is ($changed, 1, "artist change");
}

# title change
{
    my $cut = Cfm::Connector::State::TrackState->new;

    $cut->update(
        [ "a1", "a2" ],
        "title1",
        "release",
    );
    my $changed = $cut->update(
        [ "a1", "a2" ],
        "title2",
        "release",
    );

    is ($changed, 1, "title change");
}

is (Cfm::Connector::State::TrackState::_arrays_differ([ "1", "2" ], [ "1", "2" ]), 0, "same arrays");
is (Cfm::Connector::State::TrackState::_arrays_differ([ "2", "1" ], [ "1", "2" ]), 1, "order changed");
is (Cfm::Connector::State::TrackState::_arrays_differ([ "1", "2" ], [ "1", "2", "3" ]), 1, "item added");
is (Cfm::Connector::State::TrackState::_arrays_differ([ "1", "2" ], [ "1" ]), 1, "item removed");
is (Cfm::Connector::State::TrackState::_arrays_differ([ "1", "2" ], undef), 1, "rhs undef");
is (Cfm::Connector::State::TrackState::_arrays_differ(undef, undef), 0, "both undef");
is (Cfm::Connector::State::TrackState::_arrays_differ(undef, []), 1, "lhs undef");

done_testing;
