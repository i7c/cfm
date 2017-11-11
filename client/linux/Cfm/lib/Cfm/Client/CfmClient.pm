package Cfm::Client::CfmClient;

use Moo;
use strict;
use warnings;

extends 'Cfm::Client::Client';

sub create_playback {
    my ($self, $create_playback) = @_;

    my $response = $self->post_json("/rec/v1/playbacks", $create_playback->create_dto);
    return Cfm::Playback->from_hash($response);
}

sub my_playbacks {
    my ($self, $only_broken, $page) = @_;

    my @params = ();
    if ($only_broken) {
        push @params, onlyBroken => "true";
    }
    if ($page > 0) {
        push @params, page => $page;
    }

    my $response = $self->get_json("/rec/v1/playbacks", \@params);
    return  Cfm::PlaybackList->from_hash($response);
}

sub fix_playback {
    my ($self, $uuid, $create_playback, $auto) = @_;

    my $playback;
    if ($auto) {
        $playback = $self->patch_json("/rec/v1/playbacks/$uuid", +{}, [ auto => "true" ]);
    } else {
        $playback = $self->patch_json("/rec/v1/playbacks/$uuid", $create_playback->_to_hash);
    }
    return Cfm::Playback->from_hash($playback);
}

sub create_invite {
    my ($self) = @_;

    my $params = ();
    my $response = $self->post_json("/rec/v1/invites", +{}, $params);
    return Cfm::Dto::Invite->from_hash($response);
}

1;
