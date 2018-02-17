package Cfm::Ui::Format::Csv;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
with 'Cfm::Ui::Format::Formatter';

use Cfm::Autowire;
use Text::CSV;

has csv => (
        is      => 'lazy',
        default => sub {Text::CSV->new({eol => "\n"})},
    );

sub playback_list {
    my ($self, $pbl) = @_;

    for my $pb (@{$pbl->elements}) {
        $self->playback($pb);
    }
}

sub playback {
    my ($self, $pb) = @_;

    $self->csv->print(\*STDOUT, [ $pb->artists->[0], $pb->recordingTitle, $pb->releaseTitle, $pb->timestamp,
            $pb->broken ]);
}

sub user {
    my ($self, $user) = @_;

    $self->csv->print(\*STDOUT, [ $user->name, $user->state, $user->systemUser ]);
}

sub accumulated_playbacks {
    my ($self, $acc_playbacks) = @_;

    for my $acc ($acc_playbacks->elements->@*) {
        $self->csv->print(\*STDOUT, [ $acc->occurrences, $acc->artists->[0], $acc->recordingTitle,
                $acc->releaseTitle ]);
    }
}

sub release_groups {
    my ($self, $rgs) = @_;

    for my $rg ($rgs->elements->@*) {
        $self->csv->print(\*STDOUT, [ $rg->artists->[0], $rg->name, $rg->id ]);
    }
}

sub affected {
    my ($self, $affected) = @_;

    $self->csv->print(\*STDOUT, [ $affected->affected ]);
}

1;
