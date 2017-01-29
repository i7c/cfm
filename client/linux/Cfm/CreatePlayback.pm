package Cfm::CreatePlayback;

use strict;
use warnings;
use Moo;
use Carp;

has mbTrackId => (is => 'rw');

has mbReleaseGroupId => (is => 'rw');

has artist => (is => 'rw');

has title => (is => 'rw');

has album => (is => 'rw');

# Returns the wire representation of this object to send to the server
sub dto {
    my ($self) = @_;

    my %result = ();
    if ($self->mbTrackId || ($self->artist && $self->title && $self->album)) {
        $result{mbTrackId} = $self->mbTrackId;
        $result{mbReleaseGroupId} = $self->mbReleaseGroupId if $self->mbReleaseGroupId;
        $result{artist} = $self->artist if $self->artist;
        $result{title} = $self->title if $self->title;
        $result{album} = $self->album if $self->album;
        return \%result;
    } else {
        croak "Insufficient data to create a new playback. Provide at least mbTrackId.";
    }
}

1;