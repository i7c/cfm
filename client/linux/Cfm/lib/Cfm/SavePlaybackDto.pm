package Cfm::SavePlaybackDto;

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

# Returns the wire representation of this object for creating a new playback
sub create_dto {
    my ($self) = @_;

    if ($self->mbTrackId || ($self->artists && $self->title && $self->album)) {
        return $self->_to_hash;
    } else {
        croak "Insufficient data to create a new playback.";
    }
}

# Returns the wire representatio of this object for updating a playback
sub fix_dto {
    my ($self) = @_;
    return $self->_to_hash;
}

sub _to_hash {
    my ($self) = @_;

    my %result = ();
    $result{mbTrackId} = $self->mbTrackId if defined $self->mbTrackId;
    $result{mbReleaseGroupId} = $self->mbReleaseGroupId if defined $self->mbReleaseGroupId;
    $result{artists} = $self->artists if defined $self->artists;
    $result{title} = $self->title if defined $self->title;
    $result{album} = $self->album if defined $self->album;
    $result{length} = $self->length if defined $self->length;
    $result{discNumber} = $self->discNumber if defined $self->discNumber;
    $result{trackNumber} = $self->trackNumber if defined $self->trackNumber;
    $result{playTime} = $self->playTime if defined $self->playTime;
    return \%result;
}

1;
