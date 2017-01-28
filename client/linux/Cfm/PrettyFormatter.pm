package Cfm::PrettyFormatter;
use strict;
use warnings FATAL => 'all';
use Moo;
use Text::ASCIITable;

use Cfm::Formatter;
use Cfm::Artist;
use Cfm::ArtistList;

extends 'Cfm::Formatter';


sub artist {
    my ($self, $artist) = @_;

    my $table = Text::ASCIITable->new({ headingText => $artist->name });
    $table->setCols("Field", "Value");
    $table->addRow("Name", $artist->name);
    $table->addRow("Identifier", $artist->identifier);
    $table->addRow("Mbid", $artist->mbid);
    print $table;
}

sub artist_list {
    my ($self, $list) = @_;

    my $table = Text::ASCIITable->new({ headingText => 'Artists' });
    $table->setCols("Name", "Identifier", "Mbid");
    for my $artist (@{$list->elements}) {
        $table->addRow($artist->name, $artist->identifier, $artist->mbid);
    }
    print $table;
}

sub playback {
    my ($self, $pb) = @_;

    my $table = Text::ASCIITable->new({ headingText => 'Playback' });
    $table->setCols("Field", "Value");
    for my $artist (@{$pb->recording->artists}) {
        $table->addRow("Artist", $artist->name);
    }
    $table->addRow("Title", $pb->recording->title);
    $table->addRow("Time", $pb->time);
    print $table;
}

sub playback_list {
    my ($self, $pbl) = @_;

    my $table = Text::ASCIITable->new({ headingText => "Playbacks" });
    $table->setCols("Identifier", "Time", "Artist", "Title", "Album");
    for my $pb (@{$pbl->elements}) {
        $table->addRow($pb->identifier, $pb->time,
            $self->name_list($pb->recording->artists),
            $pb->recording->title,
            $pb->releaseGroup->title);
    }
    print $table;
}


1;