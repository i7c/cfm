package Cfm::Playback::PlaybackClient;

use strict;
use warnings;
use Moo;
use Cfm::Autowire;

use Cfm::Common::AffectedRes;
use Cfm::Common::ListRes;
use Cfm::Playback::AccumulatedPlaybacks;
use Cfm::Playback::BatchResultRes;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackBatchRes;

with 'Cfm::Singleton';
extends 'Cfm::Client::Client';

has config => singleton 'Cfm::Config';

sub BUILD {
    my ($self) = @_;

    $self->init(
        $self->config->require_option("url"),
        "/rec/v1/playbacks",
        $self->config->require_option("user"),
        $self->config->require_option("pass"),
    );
}

sub my_playbacks {
    my ($self, $only_broken, $page, $psize) = @_;

    my @params = ();
    push @params, broken => "true" if $only_broken;
    push @params, page => $page if $page > 0;
    push @params, size => ($psize // 20);

    Cfm::Common::ListRes->from_hash($self->get_json("", \@params),
        sub { Cfm::Playback::Playback->from_hash($_); });
}

sub create_playback {
    my ($self, $playback) = @_;

    Cfm::Playback::Playback->from_hash(
        $self->post_json(
            "",
            Cfm::Playback::Playback->to_hash($playback),
            [ "id-method" => $self->config->require_option("id-method") ],
        )
    );
}

sub batch_create_playbacks {
    my ($self, $batch) = @_;

    Cfm::Playback::BatchResultRes->from_hash(
        $self->post_json("/batch", Cfm::Playback::PlaybackBatchRes->to_hash($batch))
    );
}

sub put_now_playing {
    my ($self, $playback) = @_;

    Cfm::Playback::Playback->from_hash(
        $self->put_json(
            "/now",
            Cfm::Playback::Playback->to_hash($playback),
            [ "id-method" => $self->config->require_option("id-method") ],
        ),
        1 # skip field checks
    );
}

sub get_now_playing {
    my ($self) = @_;

    Cfm::Playback::Playback->from_hash(
        $self->get_json("/now"),
        1
    );
}

sub delete_playbacks {
    my ($self, $source) = @_;

    die unless defined $source;
    Cfm::Common::AffectedRes->from_hash(
        $self->delete_json("", [ withSource => $source ])
    );
}

sub get_fixlog {
    my ($self, $page) = @_;

    my @params = ();
    push @params, page => $page if $page > 0;

    Cfm::Common::ListRes->from_hash(
        $self->get_json("/fixlog", \@params),
        sub { Cfm::Playback::Playback->from_hash($_) }
    );
}

1;
