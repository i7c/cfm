package Cfm::Ui::Format::Pretty;
use strict;
use warnings FATAL => 'all';
use Moo;
with 'Cfm::Singleton';
with 'Cfm::Ui::Format::Formatter';

use Text::ASCIITable;

sub playback_list {
    my ($self, $pbl) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols(" ", "Artist", "Title", "Album", "Time", "Identifier");
    for my $pb (@{$pbl->elements}) {
        my ($sound, $artist_list, $title, $album) = ("");

        $sound .= "X" if $pb->broken eq "broken";
        $artist_list = join "; ", $pb->artists->@*;
        $title = $pb->recordingTitle;
        $album = $pb->releaseTitle;

        $table->addRow($sound,
            $artist_list,
            $title,
            $album,
            Cfm::Ui::Format::Formatter::time($pb->timestamp),
            $pb->id);
    }
    print $table;
    print Cfm::Ui::Format::Formatter::list_details($pbl);
}

1;