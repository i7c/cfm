package Cfm::Ui::Format::Pretty;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
with 'Cfm::Ui::Format::Formatter';

use Cfm::Autowire;
use Cfm::Ui::Format::Common;
use Text::ASCIITable;

has row_count => (
        is      => 'lazy',
        default => sub {0},
    );

sub playback_list {
    my ($self, $pbl, $verbose, $fix_attempt) = @_;

    my $table = Text::ASCIITable->new;
    my @cols = (" ", "Artist", "Title", "Album", "Time");
    # add left
    unshift @cols, "No" if $self->row_count;
    # add right
    push @cols, "Fix" if $fix_attempt;
    push @cols, "Id" if $verbose;
    $table->setCols(@cols);

    my $i = 1;
    for my $pb (@{$pbl->elements}) {
        my ($sound, $artist_list, $title, $album) = ("");

        $sound .= "!" if $pb->broken;
        $artist_list = join "; ", $pb->artists->@*;
        $title = $pb->recordingTitle;
        $album = $pb->releaseTitle;

        my @vals = (
            $sound,
            $artist_list,
            $title,
            $album,
            Cfm::Ui::Format::Common::time($pb->timestamp),
        );
        unshift @vals, $i++ if $self->row_count;
        push @vals, Cfm::Ui::Format::Common::time($pb->fixAttempt) if $fix_attempt;
        push @vals, $pb->id if $verbose;
        $table->addRow(@vals);
    }
    print $table;
    print Cfm::Ui::Format::Common::list_details($pbl);
}

sub playback {
    my ($self, $pb) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols("Property", "Value");
    $table->addRow("Artist(s)", join(";", $pb->artists->@*));
    $table->addRow("Title", $pb->recordingTitle);
    $table->addRow("Release", $pb->releaseTitle);
    $table->addRow("Time", Cfm::Ui::Format::Common::time($pb->timestamp));
    $table->addRow("Broken", Cfm::Ui::Format::Common::boolean($pb->broken));
    print $table;
}

sub user {
    my ($self, $user) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols("Property", "Value");
    $table->addRow("Name", $user->name);
    $table->addRow("State", $user->state);
    $table->addRow("System User", $user->systemUser);
    print $table;
}

sub accumulated_playbacks {
    my ($self, $acc_playbacks) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols("Occ", "Artists", "Title", "Release");
    for my $acc ($acc_playbacks->elements->@*) {
        $table->addRow($acc->occurrences, join("; ", $acc->artists->@*), $acc->recordingTitle, $acc->releaseTitle);
    }
    print $table;
    print Cfm::Ui::Format::Common::list_details($acc_playbacks);
}

sub accumulated_playback {
    my ($self, $acc) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols("Property", "Value");
    for my $artist ($acc->artists->@*) {
        $table->addRow("Artist", $artist);
    }
    $table->addRow("Title", $acc->recordingTitle);
    $table->addRow("Release", $acc->releaseTitle);
    $table->addRow("Occurences", $acc->occurrences);
    print $table;
}

sub release_groups {
    my ($self, $rgs) = @_;

    my $table = Text::ASCIITable->new;
    my @cols = ("Artists", "Title", "Id");
    unshift @cols, "No" if $self->row_count;
    $table->setCols(@cols);

    my $i = 1;
    for my $rg ($rgs->elements->@*) {
        my @vals = (join("; ", $rg->artists->@*), $rg->name, $rg->id);
        unshift @vals, $i++ if $self->row_count;
        $table->addRow(@vals);
    }
    print $table;
    print Cfm::Ui::Format::Common::list_details($rgs);
}

sub recordings {
    my ($self, $recs) = @_;

    my $table = Text::ASCIITable->new;
    my @cols = ("Artists", "Name", "Length", "Id");
    unshift @cols, "No" if $self->row_count;
    $table->setCols(@cols);

    my $i = 1;
    for my $rec ($recs->elements->@*) {
        my @vals = (join("; ", $rec->artists->@*), $rec->name, $rec->length, $rec->id);
        unshift @vals, $i++ if $self->row_count;
        $table->addRow(@vals);
    }
    print $table;
    print Cfm::Ui::Format::Common::list_details($recs);
}

sub review_acc_fix {
    my ($self, $acc, $rg, $rec) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols("Property", "Old", "New");

    $table->addRow("Artists", join("; ", $acc->artists->@*), join("; ", $rec->artists->@*));
    $table->addRow("Title", $acc->recordingTitle, $rec->name);
    $table->addRow("Release", $acc->releaseTitle, $rg->name);
    $table->addRow("Rec ID", "", $rec->id);
    $table->addRow("Rg ID", "", $rg->id);
    print $table;
}

sub affected {
    my ($self, $affected) = @_;

    my $num = $affected->affected;
    print "$num elements affected.\n";
}

1;
