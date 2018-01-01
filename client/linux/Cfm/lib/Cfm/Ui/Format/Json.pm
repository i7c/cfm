package Cfm::Ui::Format::Json;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
with 'Cfm::Ui::Format::Formatter';

use Cfm::Autowire;
use Cfm::Playback::Playback;
use Encode;
use JSON::MaybeXS;

sub playback_list {
    my ($self, $pbl) = @_;

    my %res = (
        elements   => [ map {Cfm::Playback::Playback->to_hash($_)} $pbl->elements->@* ],
        size       => $pbl->size,
        count      => $pbl->count,
        number     => $pbl->number,
        total      => $pbl->total,
        totalPages => $pbl->totalPages,
    );

    $self->_print(encode_json(\%res));
}

sub playback {
    my ($self, $pb) = @_;

    $self->_print(encode_json(Cfm::Playback::Playback->to_hash($pb)));
}

sub user {
    my ($self, $user) = @_;

    $self->_print(encode_json(fm::User::User->to_hash($user)));
}

sub _print {
    my ($self, $x) = @_;

    # Let's fool perl's IO layer. encode_json actually writes a raw byte stream, but somehow fails to set the
    # utf8 flag. This leads to perl's IO layer trying to encode everything *again*. Not nice, but works for now. :)
    Encode::_utf8_on($x);
    print $x;
}

1;
