package Cfm::Ui::Wizard::FixAccPlaybackWizard;
use strict;
use warnings FATAL => 'all';
use Log::Any qw/$log/;
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;
use Cfm::Mb::MbService;
use Cfm::Playback::PlaybackService;
use Cfm::Ui::Selector::Selector;
use Term::Form;
use Try::Tiny;

has form => (is => 'ro', default => sub {Term::Form->new('name')});
has formatter => autowire(
        'Cfm::Ui::Format::Pretty' => 'Cfm::Ui::Format::Formatter',
        row_count                 => 1,
    );
has mbservice => singleton 'Cfm::Mb::MbService';
has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub run {
    my ($self, $acc) = @_;

    # attempt automatic fixing
    return if $self->fix_acc_playback_automatically($acc);
    # fall back to manual fixing
    return if $self->fix_acc_playback_manually($acc);
    # mark as unfixable for now?
    $self->mark_unfixable($acc);
}

sub fix_acc_playback_automatically {
    my ($self, $acc) = @_;

    my $success = 0;
    try {
        my $rgs = $self->mbservice->identify_release_group($acc->artists, $acc->releaseTitle);
        die $log->fatal("Could not find any release") unless scalar $rgs->elements->@* > 0;
        my $rg = $rgs->elements->[0];
        my $recs = $self->mbservice->identify_recording($rg->id, $acc->recordingTitle);
        die $log->fatal("Could not find any recordigns") unless scalar $recs->elements->@* > 0;
        my $rec = $recs->elements->[0];
        if ($self->review($acc, $rg, $rec)) {
            $self->_fix($acc, $rg, $rec);
            $success = 1;
        }
    };
    $success;
}

sub fix_acc_playback_manually {
    my ($self, $acc) = @_;

    my $success = 0;
    try {
        $self->formatter->accumulated_playback($acc);
        my $artists = $self->determine_artists($acc->artists);
        die $log->fatal("No artists to lookup") unless scalar $artists->@* > 0;
        my $rg = $self->determine_rg_id($artists, $acc->releaseTitle);
        die $log->fatal("No release group id found") unless defined $rg;
        my $rec = $self->determine_rec_id($rg->id, $acc->recordingTitle);
        die $log->fatal("No recording id found") unless defined $rec;
        if ($self->review($acc, $rg, $rec)) {
            $self->_fix($acc, $rg, $rec);
            $success = 1;
        }
    };
    $success;
}

sub mark_unfixable {
    my ($self, $acc) = @_;

    my $mark_unfixable = Cfm::Ui::Selector::Selector::yes_no("Skip this playback for now");
    if ($mark_unfixable) {
        die $log->fatal("Skipping")
    }
}

sub determine_artists {
    my ($self, $artist_names) = @_;

    my @consolidated;
    map {
        my $ca = $self->_prompt("> ", $_);
        push @consolidated, $ca if $ca =~ /\S+/;
    } $artist_names->@*;
    \@consolidated;
}

sub determine_rg_id {
    my ($self, $artists, $release_title) = @_;

    my $selected_rg = Cfm::Ui::Selector::Selector::numerical_select(
        sub {$self->mbservice->identify_release_group($artists, $release_title, $_[0])},
        sub {$self->formatter->release_groups($_[0])},
    );
    die $log->fatal("No release group selected.") unless defined $selected_rg;
    $selected_rg;
}

sub determine_rec_id {
    my ($self, $rg_id, $recording_title) = @_;

    my $selected_rec = Cfm::Ui::Selector::Selector::numerical_select(
        sub {$self->mbservice->identify_recording($rg_id, $recording_title, $_[0])},
        sub {$self->formatter->recordings($_[0])},
    );
    die $log->fatal("No recording selected.") unless defined $selected_rec;
    $selected_rec;
}

sub review {
    my ($self, $acc, $rg, $rec) = @_;

    $self->formatter->review_acc_fix($acc, $rg, $rec);
    Cfm::Ui::Selector::Selector::yes_no("Is this correct?");
}

sub _prompt {
    my ($self, $prompt, $default) = @_;

    $self->form->readline($prompt, {default => $default});
}

sub _fix {
    my ($self, $acc, $rg, $rec) = @_;

    $acc->releaseGroupId($rg->id);
    $acc->recordingId($rec->id);
    $self->playback_service->fix_acc_playback($acc);
}

1;
