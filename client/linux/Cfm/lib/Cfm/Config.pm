package Cfm::Config;

use strict;
use warnings FATAL => 'all';
use Moo;
use Log::Any qw($log);
with 'Cfm::Singleton';

use Config::Simple;

my @config_locations = (
    $ENV{'HOME'} . "/.cfm.conf",
    $ENV{'HOME'} . "/.config/cfm/config",
    "/etc/cfm.conf"
);

my %conf_default = (

);

has conf => (is => 'rw');
has options => (is => 'rw');

sub BUILD {
    my ($self, $args) = @_;

    $self->options(+ {});
    for my $conf_file (@config_locations) {
        $log->debug("Try location " . $conf_file);
        if (-f $conf_file) {
            $log->info("Found config file: $conf_file");
            $self->conf(Config::Simple->new($conf_file));
            return;
        }
    }
    $log->info("Ran out of config file locations. Proceed without configuration file.");
}

sub get_option {
    my ($self, $option) = @_;

    $self->options->{$option}
        // do {$self->conf->param($option) if $self->conf;}
        // $conf_default{$option};
}

sub require_option {
    my ($self, $option) = @_;

    $self->get_option($option) or die $log->error("You must provide the $option option.");
}

1;