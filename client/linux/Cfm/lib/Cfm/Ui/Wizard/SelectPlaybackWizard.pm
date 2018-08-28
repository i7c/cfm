package Cfm::Ui::Wizard::SelectPlaybackWizard;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;

has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub select_accumulated_broken {
    my ($self) = @_;

    my $accs = $self->playback_service->accumulated_broken_playbacks(0);
    return undef unless (scalar $accs->elements->@*) > 0;
    $accs->elements->[0];
}

1;
