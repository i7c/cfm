package Cfm::Client::CfmClient;

use strict;
use warnings;
use Moo;
use Cfm::Autowire;

use Cfm::Common::ListRes;
use Cfm::Config;
use Cfm::Playback::AccumulatedPlaybacks;
use Cfm::Playback::BatchResultRes;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackBatchRes;

with 'Cfm::Singleton';
extends 'Cfm::Client::Client';

has config => singleton 'Cfm::Config';

sub BUILD {
    my ($self, $args) = @_;

    $self->url($self->config->require_option("url"));
    my $user = $self->config->require_option("user");
    my $pass = $self->config->require_option("pass");
    $self->headers->authorization_basic($user, $pass);
}

sub my_playbacks {
    my ($self, $only_broken, $page) = @_;

    my @params = ();
    push @params, broken => "true" if $only_broken;
    push @params, page => $page if $page > 0;

    Cfm::Common::ListRes->from_hash($self->get_json("/rec/v1/playbacks", \@params),
        sub {
            Cfm::Playback::Playback->from_hash($_);
        });
}

sub create_playback {
    my ($self, $playback) = @_;

    Cfm::Playback::Playback->from_hash(
        $self->post_json("/rec/v1/playbacks", Cfm::Playback::Playback->to_hash($playback))
    );
}

sub batch_create_playbacks {
    my ($self, $batch) = @_;

    Cfm::Playback::BatchResultRes->from_hash(
        $self->post_json("/rec/v1/playbacks/batch", Cfm::Playback::PlaybackBatchRes->to_hash($batch))
    );
}

sub post_user {
    my ($self, $user) = @_;

    Cfm::User::User->from_hash(
        $self->post_json("/rec/v1/users", Cfm::User::User->to_hash($user))
    );
}

sub put_now_playing {
    my ($self, $playback) = @_;

    Cfm::Playback::Playback->from_hash(
        $self->put_json("/rec/v1/playbacks/now", Cfm::Playback::Playback->to_hash($playback)),
        1 # skip field checks
    );
}

sub get_now_playing {
    my ($self) = @_;

    Cfm::Playback::Playback->from_hash(
        $self->get_json("/rec/v1/playbacks/now"),
        1
    );
}

sub get_accumulated_playbacks {
    my ($self, $page) = @_;

    my @params = ();
    push @params, page => $page if $page > 0;

    Cfm::Common::ListRes->from_hash($self->get_json("/rec/v1/playbacks/acc", \@params),
        sub {
            Cfm::Playback::AccumulatedPlaybacks->from_hash($_);
        });
}

sub post_accumulated_playbacks {
    my ($self, $acc) = @_;

    $self->post_json("/rec/v1/playbacks/acc", Cfm::Playback::AccumulatedPlaybacks->to_hash($acc))
}

1;
