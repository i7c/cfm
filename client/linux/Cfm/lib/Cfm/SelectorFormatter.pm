package Cfm::SelectorFormatter;
use strict;
use warnings FATAL => 'all';
use Moo;
use Text::ASCIITable;

use Cfm::Formatter;

extends 'Cfm::Formatter';


sub playback_list {
    my ($self, $pbl) = @_;

    my $table = Text::ASCIITable->new;
    $table->setCols("No", " ", "Identifier", "Time", "Artist", "Title", "Album");
    my $counter = 1;
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

        $table->addRow($counter,
            $sound,
            $pb->identifier,
            $self->time($pb->time),
            $artist_list,
            $title,
            $album);
        $counter++;
    }
    print $table;
    print $self->list_details($pbl);
}

sub mb_releasegroup_list {
    my ($self, $rgs) = @_;

    my $counter = 1;

    my $table = Text::ASCIITable->new({ headingText => "Album Groups" });
    $table->setCols("No", "Identifier", "Artists", "Album Name", "Comment");
    for my $rg ($rgs->elements->@*) {
        my @artist_names = map { $_->displayName } $rg->artistReferences->@*;
        $table->addRow($counter, $rg->identifier, join(", ", @artist_names), $rg->name, $rg->comment);
        $counter++;
    }
    print $table;
    print $self->list_details($rgs);
}

sub mb_recording_list {
    my ($self, $recs) = @_;

    my $counter = 1;
    my $table = Text::ASCIITable->new({ headingText => "Recordings" });
    $table->setCols("No", "Identifier", "Artists", "Title", "Length", "Comment");
    for my $rec ($recs->elements->@*) {
        my @artist_names = map { $_->displayName } $rec->artistReferences->@*;
        $table->addRow($counter, $rec->identifier, join(", ", @artist_names), $rec->name, $self->length($rec->length),
            $rec->comment);
        $counter++;
    }
    print $table;
    print $self->list_details($recs);
}

1;