package Cfm::CreatePlayback;

use strict;
use warnings;
use Moo;
use Carp;

has mbTrackId => (
    is => 'rw'
);

has mbReleaseGroupId => (
    is => 'rw'
);

# Returns the wire representation of this object to send to the server
sub dto {
    my $self = shift;

    my %result = ();
    if ($self->mbTrackId) {
        $result{mbTrackId} = $self->mbTrackId;
        $result{mbReleaseGroupId} = $self->mbReleaseGroupId if $self->mbReleaseGroupId;
        return \%result;
   } else {
        croak "Insufficient data to create a new playback. Provide at least mbTrackId.";
    }
}

1;