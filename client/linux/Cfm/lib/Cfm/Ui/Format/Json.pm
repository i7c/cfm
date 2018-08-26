package Cfm::Ui::Format::Json;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
with 'Cfm::Ui::Format::Formatter';

use Cfm::Autowire;
use Cfm::Playback::AccumulatedPlaybacks;
use Cfm::Playback::Playback;
use Cfm::Stats::FirstClassStats;
use Encode;
use JSON::MaybeXS;

sub playback_list {
    my ($self, $pbl) = @_;

    $self->_print(encode_json(
        $self->_list($pbl, sub {Cfm::Playback::Playback->to_hash($_)})
    ));
}

sub playback {
    my ($self, $pb) = @_;

    $self->_print(encode_json(Cfm::Playback::Playback->to_hash($pb)));
}

sub user {
    my ($self, $user) = @_;

    $self->_print(encode_json(Cfm::User::User->to_hash($user)));
}

sub accumulated_playbacks {
    my ($self, $acc_playbacks) = @_;

    $self->_print(encode_json(
        $self->_list($acc_playbacks, sub {Cfm::Playback::AccumulatedPlaybacks->to_hash($_)})
    ));
}

sub release_groups {
    my ($self, $rgs) = @_;

    $self->_print(encode_json(
        $self->_list($rgs, sub {Cfm::Mb::ReleaseGroup->to_hash($_)})
    ));
}

sub affected {
    my ($self, $affected) = @_;

    $self->_print(encode_json(Cfm::Common::AffectedRes->to_hash($affected)));
}

sub _list {
    my ($self, $list, $trans) = @_;

    + {
        elements   => [ map {$trans->($_)} $list->elements->@* ],
        size       => $list->size,
        count      => $list->count,
        number     => $list->number,
        total      => $list->total,
        totalPages => $list->totalPages,
    }
}

sub _print {
    my ($self, $x) = @_;

    # Let's fool perl's IO layer. encode_json actually writes a raw byte stream, but somehow fails to set the
    # utf8 flag. This leads to perl's IO layer trying to encode everything *again*. Not nice, but works for now. :)
    Encode::_utf8_on($x);
    print $x;
}

sub first_class_stats_list {
    my ($self, $fcsl) = @_;

    $self->_print(encode_json(
        $self->_list($fcsl, sub {Cfm::Stats::FirstClassStats->to_hash($_)})
    ));
}

1;
