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

1;
