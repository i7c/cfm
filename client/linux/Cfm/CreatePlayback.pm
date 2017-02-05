package Cfm::CreatePlayback;

use strict;
use warnings;
use Moo;
use Carp;

has mbTrackId => (is => 'rw');

has mbReleaseGroupId => (is => 'rw');

has artists => (is => 'rw');

has title => (is => 'rw');

has album => (is => 'rw');

has length => (is => 'rw');

has discNumber => (is => 'rw');

has trackNumber => (is => 'rw');

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

        return \%result;
    } else {
        croak "Insufficient data to create a new playback.";
    }
}

1;