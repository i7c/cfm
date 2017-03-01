package Cfm;

use 5.24.0;
use strict;
use warnings;

=head1 NAME

Cfm - Command Line Client for cfm

=head1 VERSION

Version 0.01

=cut

our $VERSION = '0.01';


=head1 SYNOPSIS

Provides a command line client for cfm as well as all of the backing modules to
use cfm from a perl script.

    use Cfm::Cli;

    my $cli = Cfm::Cli->new;
    $cli->run;

=head1 LICENSE AND COPYRIGHT

Copyright 2017 Constantin Weißer.

This program is released under the following license: GPLv3

=cut

1; # End of Cfm
