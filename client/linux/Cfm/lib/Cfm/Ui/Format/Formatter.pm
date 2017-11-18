package Cfm::Ui::Format::Formatter;
use strict;
use warnings FATAL => 'all';
use Moo::Role;
with 'Cfm::Singleton';

use Cfm::Ui::Format::Pretty;
use Date::Format;

sub instance {
    my ($class) = @_;

    return $Cfm::Ui::Format::Formatter::singleton //= choose_instance();
}

sub choose_instance {
    return Cfm::Ui::Format::Pretty->instance;
}

sub time {
    my ($timestamp) = @_;

    return time2str("%Y-%m-%d %H:%M:%S", $timestamp);
}

sub list_details {
    my ($list) = @_;
    return sprintf "%d / %d (%d elements total)\n", $list->number  + 1, $list->totalPages,
        $list->total;
}

sub playback_list {}

1;
