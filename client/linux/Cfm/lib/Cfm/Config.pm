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

my @cli_args = (
    'set|s=s@',
    'csv-format=s',
    'date-format=s',
    'fail-log=s',
    'page|p=i',
    'broken',
    'debug-dump-config',
    'player=s',
    'quiet|q',
    'threshold=i',
);

my %conf_default = (
    'csv-format'  => 'ben', # https://benjaminbenben.com/lastfm-to-csv/
    'date-format' => 'ben', # https://benjaminbenben.com/lastfm-to-csv/
    page          => 0,
    threshold     => 50,
);

has conf => (
        is      => 'rw',
        default => sub {
            for my $conf_file (@config_locations) {
                $log->debug("Try location " . $conf_file);
                if (-f $conf_file) {
                    $log->info("Found config file: $conf_file");
                    my %config = Config::Simple->new($conf_file)->vars;
                    return \%config;
                }
            }
            $log->info("Ran out of config file locations. Proceed without configuration file.");
            + {};
        },
    );

sub get_option {
    my ($self, $option) = @_;

    $self->conf->{$option} // $conf_default{$option};
}

sub require_option {
    my ($self, $option) = @_;

    $self->get_option($option) or die $log->error("You must provide the $option option.");
}

sub has_option {
    defined $_[0]->conf->{$_[1]};
}

sub add_flags {
    my ($self, $args) = @_;
    my %options;

    $log->debug("seen input: " . join " ", $args->@*);
    $log->info("parsing flags");
    GetOptionsFromArray($args, \%options, @cli_args);

    map {
        $log->debug("FLAG: $_=$options{$_}");
        $self->conf->{$_} = $options{$_};
    } keys %options;
    map {$log->debug("CONF: $_=" . $self->conf->{$_})} keys $self->conf->%* if $self->has_option("debug-dump-config");
    $log->debug("remaining input: " . join " ", $args->@*);
}

sub kv_store {
    my ($self) = @_;

    return {
        map {m/^\s*([^:]*[^:\s]+)\s*:\s*(.*[^\s]+)\s*$/} $self->conf->{set}->@*
    }
}

1;
