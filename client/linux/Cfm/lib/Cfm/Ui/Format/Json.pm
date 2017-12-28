package Cfm::Ui::Format::Json;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
with 'Cfm::Ui::Format::Formatter';

use Cfm::Autowire;
use Cfm::Playback::Playback;
use JSON::MaybeXS;

sub playback_list {
    my ($self, $pbl) = @_;

    my % res = (
        elements   => [ map {Cfm::Playback::Playback->to_hash($_)} $pbl->elements->@* ],
        size       => $pbl->size,
        count      => $pbl->count,
        number     => $pbl->number,
        total      => $pbl->total,
        totalPages => $pbl->totalPages,
    );

    print JSON::encode_json(\%res);
}

sub playback {
    my ($self, $pb) = @_;

    print JSON::encode_json(Cfm::Playback::Playback->to_hash($pb));
}

sub user {
    my ($self, $user) = @_;

    print JSON::encode_json(Cfm::User::User->to_hash($user));
}

1;
