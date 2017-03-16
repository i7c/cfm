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
    print $self->list_details($list);
}

sub playback {
    my ($self, $pb) = @_;

    my $table = Text::ASCIITable->new({ headingText => 'Playback' });
    $table->setCols("Field", "Value");
    if (defined $pb->recording) {
        for my $artist (@{$pb->recording->artists}) {
            $table->addRow("Artist", $artist->name);
        }
        $table->addRow("Recording", $pb->recording->title);
    }
    if (defined $pb->releaseGroup) {
        $table->addRow("Release Group", $pb->releaseGroup->title);
    }
    $table->addRow("Time", $self->time($pb->time));
    if (defined $pb->releaseGroup) {
        $table->addRow("RELEASE GROUP", $pb->releaseGroup->mbid);
    }
    if (defined $pb->recording) {
        $table->addRow("RECORDING", $pb->recording->mbid);
    }
    for my $org_artist ($pb->originalArtists->@*) {
        $table->addRow("Original Artist", $org_artist);
    }
    $table->addRow("Original Title", $pb->originalTitle);
    $table->addRow("Original Album", $pb->originalAlbum);
    $table->addRow("Broken",
        $self->boolean(!(defined $pb->recording && defined $pb->releaseGroup)));
    print $table;
}

sub playback_list {
    my ($self, $pbl) = @_;

    my $table = Text::ASCIITable->new({ headingText => "Playbacks" });
    $table->setCols(" ", "Identifier", "Time", "Artist", "Title", "Album");
    for my $pb (@{$pbl->elements}) {
        my ($sound, $artist_list, $title, $album);

        if (defined $pb->recording) {
            $sound = " ";
            $artist_list = $self->name_list($pb->recording->artists);
            $title = $pb->recording->title;
        } else {
            $sound = "X";
            $artist_list = join(", ", $pb->originalArtists->@*);
            $title = $pb->originalTitle;
        }

        if (defined $pb->releaseGroup) {
            $album = $pb->releaseGroup->title;
        } else {
            $album = $pb->originalAlbum;
        }

        $table->addRow($sound, $pb->identifier, $self->time($pb->time),
            $artist_list,
            $title,
            $album);
    }
    print $table;
    print $self->list_details($pbl);
}


1;