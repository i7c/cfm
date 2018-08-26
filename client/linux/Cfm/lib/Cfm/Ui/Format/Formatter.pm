package Cfm::Ui::Format::Formatter;
use strict;
use warnings FATAL => 'all';
use Moo::Role;
use Switch;

requires qw/
    affected
    first_class_stats_list
    playback
    playback_list
    release_groups
    user
    /;

sub show {
    my ($self, $any) = @_;

    switch (ref $any) {
        case 'Cfm::Common::ListRes' { $self->show_list($any); }
    }
}

sub show_list {
    my ($self, $any_list) = @_;

    return unless scalar $any_list->elements->@* > 0;

    switch (ref $any_list->elements->[0]) {
        case 'Cfm::Stats::FirstClassStats' {
            $self->first_class_stats_list($any_list);
        }
    }
}

1;
