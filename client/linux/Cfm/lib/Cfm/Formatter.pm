package Cfm::Formatter;
use strict;
use warnings FATAL => 'all';
use Moo;
use Carp;
use Date::Format;

# Format a list of resources into a list of their names
sub name_list {
    my ($self, $objlist) = @_;

    my @name_list = map {
        $_->name
    } @$objlist;
    return join(", ", @name_list);
}

sub time {
    my ($self, $timestamp) = @_;

    return time2str("%Y-%m-%d %H:%M:%S", $timestamp / 1000);
}

sub list_details {
    my ($self, $list) = @_;
    return sprintf "%d / %d (%d elements total)\n", $list->pageNumber  + 1, $list->totalPages,
        $list->totalElements;
}

# Format a single artist
sub artist {
    carp "not implemented";
}

# Format a list of artists
sub artist_list {
    carp "not implemented";
}

# Format a single playback
sub playback {
    carp "not implemented";
}

# Format a list of playbacks
sub playback_list {
    carp "not implemented";
}

1;