package Cfm::Ui::Format::Common;
use strict;
use warnings FATAL => 'all';
use Moo;
use Date::Format;

sub boolean {
    my ($bool) = @_;
    return "true" if $bool;
    return "false";
}

sub time {
    my ($timestamp) = @_;

    return time2str("%Y-%m-%d %H:%M:%S", $timestamp);
}

sub list_details {
    my ($list) = @_;
    sprintf "%d of %d (%d elements total)\n",
        $list->number + 1,
        $list->totalPages,
        $list->total;
}

1;
