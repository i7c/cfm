package Cfm::CreatePlayback;

use strict;
use warnings;
use Moo;
use Carp;

has mbTrackId => (is => 'ro');

has mbReleaseGroupId => (is => 'ro');

has artists => (is => 'ro');

has title => (is => 'ro');

has album => (is => 'ro');

has length => (is => 'ro');

has discNumber => (is => 'ro');

has trackNumber => (is => 'ro');

has playTime => (is => 'ro');

# Returns the wire representation of this object to send to the server
sub dto {
    my ($self) = @_;

    my %result = ();
    if ($self->mbTrackId || ($self->artists && $self->title && $self->album)) {
        $result{mbTrackId} = $self->mbTrackId if  defined $self->mbTrackId;
        $result{mbReleaseGroupId} = $self->mbReleaseGroupId if defined $self->mbReleaseGroupId;
        $result{artists} = $self->artists if defined $self->artists;
        $result{title} = $self->title if defined $self->title;
        $result{album} = $self->album if defined $self->album;
        $result{length} = $self->length if defined $self->length;
        $result{discNumber} = $self->discNumber if defined $self->discNumber;
        $result{trackNumber} = $self->trackNumber if defined $self->trackNumber;
        $result{playTime} = $self->playTime if defined $self->playTime;

        return \%result;
    } else {
        croak "Insufficient data to create a new playback.";
    }
}

1;