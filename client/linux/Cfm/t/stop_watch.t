use strict;
use warnings FATAL => 'all';
use Test::More tests => 5;

use Cfm::Connector::State::StopWatch;

# single trip
{
    my $cut = Cfm::Connector::State::StopWatch->new;

    $cut->start_at(10);
    $cut->stop_at(15);

    is ($cut->reset, 5, "elapsed time single trip");
}

# multiple trips
{
    my $cut = Cfm::Connector::State::StopWatch->new;

    $cut->start_at(10);
    $cut->stop_at(15);
    $cut->start_at(20);
    $cut->stop_at(21);
    $cut->start_at(10000);
    $cut->stop_at(10500);

    is ($cut->reset, 506, "elapsed time multi trip");
}

# multi start does not hurt
{
    my $cut = Cfm::Connector::State::StopWatch->new;

    $cut->start_at(10);
    $cut->start_at(19);
    $cut->start_at(50);
    $cut->stop_at(60);

    is ($cut->reset, 50, "multi start does not hurt");
}

# multi stop does not hurt
{
    my $cut = Cfm::Connector::State::StopWatch->new;

    $cut->start_at(10);
    $cut->stop_at(20);
    $cut->stop_at(30);
    $cut->stop_at(34);

    is ($cut->reset, 10, "multi stop does not hurt");
}

# multi reset
{
    my $cut = Cfm::Connector::State::StopWatch->new;

    $cut->start_at(10);
    $cut->stop_at(20);
    $cut->reset;

    is ($cut->reset, 0, "second reset gets 0");
}
