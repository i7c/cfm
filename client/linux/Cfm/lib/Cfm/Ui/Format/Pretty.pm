package Cfm::Ui::Format::Pretty;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
with 'Cfm::Ui::Format::Formatter';

use Cfm::Autowire;
use Cfm::Ui::Format::Common;
use Text::ASCIITable;

has common => singleton "Cfm::Ui::Format::Common";

sub playback_list {
    my ($self, $pbl) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols(" ", "Artist", "Title", "Album", "Time", "Identifier");
    for my $pb (@{$pbl->elements}) {
        my ($sound, $artist_list, $title, $album) = ("");

        $sound .= "!" if $pb->broken;
        $artist_list = join "; ", $pb->artists->@*;
        $title = $pb->recordingTitle;
        $album = $pb->releaseTitle;

        $table->addRow($sound,
            $artist_list,
            $title,
            $album,
            $self->common->time($pb->timestamp),
            $pb->id);
    }
    print $table;
    print $self->common->list_details($pbl);
}

sub playback {
    my ($self, $pb) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols("Property", "Value");
    $table->addRow("Artist(s)", join(";", $pb->artists->@*));
    $table->addRow("Title", $pb->recordingTitle);
    $table->addRow("Release", $pb->releaseTitle);
    $table->addRow("Time", $self->common->time($pb->timestamp));
    $table->addRow("Broken", $self->common->boolean($pb->broken));
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
    print $self->common->list_details($acc_playbacks);
}

1;
