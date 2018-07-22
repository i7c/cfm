package Cfm::User::User;
use strict;
use warnings FATAL => 'all';
use Moo;
with "Cfm::Common::Res";

$Cfm::User::User::active = "ACTIVE";
$Cfm::User::User::inactive = "INACTIVE";

%Cfm::User::User::projection = (
    systemUser => sub {"true" if $_[0]},
);

%Cfm::User::User::mapping = (
    systemUser => \&Cfm::Common::Res::_ds_boolean
);

%Cfm::User::User::projection = (
    systemUser => \&Cfm::Common::Res::_s_boolean,
);

has name => (is => 'ro');
has password => (is => 'ro');
has state => (is => 'ro');
has systemUser => (is => 'ro');

sub valid {
    my ($self) = @_;

    defined $self->name
        && length($self->password) >= 8
        && defined $self->state;
}

1;
