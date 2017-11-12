package Cfm::Config;

use strict;
use warnings FATAL => 'all';
use Moo;
use Log::Any qw($log);
with 'Cfm::Singleton';

use Config::Simple;
use Getopt::Long qw/GetOptionsFromArray/;

my @config_locations = (
    $ENV{'HOME'} . "/.cfm.conf",
    $ENV{'HOME'} . "/.config/cfm/config",
    "/etc/cfm.conf"
);

my %conf_default = (

);

has conf => (
        is      => 'rw',
        default => sub {+ {}},
    );

sub BUILD {
    my ($self, $args) = @_;

    for my $conf_file (@config_locations) {
        $log->debug("Try location " . $conf_file);
        if (-f $conf_file) {
            $log->info("Found config file: $conf_file");
            my %config = Config::Simple->new($conf_file)->vars;
            $self->conf(\%config);
            return;
        }
    }
    $log->info("Ran out of config file locations. Proceed without configuration file.");
}

sub get_option {
    my ($self, $option) = @_;

    $self->conf->{$option} // $conf_default{$option};
}

sub require_option {
    my ($self, $option) = @_;

    $self->get_option($option) or die $log->error("You must provide the $option option.");
}

1;