package Cfm::Ui::Format::Pretty;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton', 'Cfm::Ui::Format::Formatter';

use Cfm::Autowire;
use Cfm::Ui::Format::Common;
use Text::ASCIITable;

has config => singleton 'Cfm::Config';
has row_count => (
        is      => 'lazy',
        default => sub {0},
    );

sub first_class_stats_list {
    my ($self, $fcsl) = @_;

    my @cols = ('Count', 'Artists', 'Title');

    _print_table(
        $fcsl->elements,
        [@cols],
        Count => 'count',
        Artists => sub { join("; ", $_[0]->artists->@*)},
        Title => 'title',
    );
}

sub playback_list {
    my ($self, $pbl, $verbose, $fix_attempt) = @_;

    my @cols = $self->config->require_option("list-cols")->@*;
    @cols = _append_missing("Fix", @cols) if $fix_attempt;
    @cols = _append_missing("Id", @cols) if $verbose;

    _print_table(
        $pbl->elements,
        [@cols],
        '.' => sub{ $_[0]->broken ? '  !' : "\x{2713}  " },
        '!' => sub{ $_[0]->broken ? '!' : undef },
        Artist => sub{join("; ", $_[0]->artists->@*)},
        Title => 'recordingTitle',
        Album => 'releaseTitle',
        Time => sub{ Cfm::Ui::Format::Common::time($_[0]->timestamp) },
        Id => 'id',
        Fix => sub{ Cfm::Ui::Format::Common::time($_[0]->fixAttempt) },
        '#' => sub{ $_[2] + 1 },
    );
    print Cfm::Ui::Format::Common::list_details($pbl);
}

sub playback {
    my ($self, $pb) = @_;

    _print_table(
        [
            [Artists => join("; ", $pb->artists->@*)],
            [Title => $pb->recordingTitle],
            [Release => $pb->releaseTitle],
            [Time => Cfm::Ui::Format::Common::time($pb->timestamp)],
            [Attached => Cfm::Ui::Format::Common::boolean(!$pb->broken)],
            [Id => $pb->id],
            [Source => $pb->source],
            ['Last fix attempt' => $pb->fixAttempt],
        ],
        ["Property", "Value"],
        Property => sub{ $_[0]->[0] },
        Value => sub{ $_[0]->[1] },
    );
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

sub kv {
  my ($self, $kv) = @_;

  _print_table(
    [map { [ $_ => $kv->{$_} ] } keys $kv->%*],
    ['Key', 'Val'],
    Key => sub { $_[0]->[0] },
    Val => sub { $_[0]->[1] },
  );
}

sub _print_table {
    my ($data, $cols, %mappings) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols($cols->@*);

    my $index = 0;
    for my $e ($data->@*) {
        my @row;
        for my $c ($cols->@*) {
            my $accessor = $mappings{$c};
            if (ref $accessor eq 'CODE') {
                push @row, $accessor->($e, $data, $index);
            } elsif (!ref $accessor && defined $accessor) {
                push @row, $e->{$accessor};
            } elsif (defined $e->{$c}) {
                push @row, $e->{$c};
            } else {
                push @row, undef;
            }
        }
        $table->addRow(@row);
        $index++;
    }

    print $table;
}

sub _append_missing {
    my ($col, @cols) = @_;

    if (! grep /^$col$/, @cols) {
        push @cols, $col;
    }
    @cols;
}

1;
