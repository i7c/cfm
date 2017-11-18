package Cfm::Ui::Format::Common;
use strict;
use warnings FATAL => 'all';
use Moo;
use Cfm::Singleton;
with 'Cfm::Singleton';

use Date::Format;

sub boolean {
    my ($self, $bool) = @_;
    return "true" if $bool;
    return "false";
}

sub time {
    my ($self, $timestamp) = @_;

    return time2str("%Y-%m-%d %H:%M:%S", $timestamp);
}

sub list_details {
    my ($self, $list) = @_;
    return sprintf "%d of %d (%d elements total)\n", $list->number + 1, $list->totalPages, $list->total;
}

1;
