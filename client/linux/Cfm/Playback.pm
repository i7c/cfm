package Cfm::Playback;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Resource;

extends "Cfm::Resource";

my @mandatory = (
    "recording",
    "time",
    "userRef"
);

has time => (
        is => 'ro'
    );

sub _mandatory_fields {
    return \@mandatory;
}

1;