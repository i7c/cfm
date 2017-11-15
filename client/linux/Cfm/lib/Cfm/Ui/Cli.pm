package Cfm::Ui::Cli;
use strict;
use warnings FATAL => 'all';
use Moo;
use Cfm::Autowire;
use Log::Any qw($log);

use Cfm::Config;
use Cfm::Playback::PlaybackService;

my %command_mapping = (
);

has config => singleton 'Cfm::Config';
has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub run {
    my ($self) = @_;

    my @args = @ARGV;
    $self->config->add_flags(\@args);
    my ($cmd, $cmdargs) = $self->greedy_match_command(\@args);
    $log->debug("$cmd args: " . join " ", $cmdargs->@*);

    $command_mapping{$cmd}->($self, $cmdargs->@*);
}

sub greedy_match_command {
    my ($self, $command) = @_;
    my $c = scalar($command->@*);
    my $match = "";
    my $match_index = - 1;

    for (my $i = 0; $i < $c; $i++) {
        my $candidate = join "-", @{$command}[0 .. $i];
        if (defined $command_mapping{$candidate}) {
            $match_index = $i;
            $match = $candidate;
        }
    }
    if ($match_index >= 0) {
        return ($match, [ @{$command}[$match_index + 1 .. $c - 1] ])
    } else {
        die $log->error("No such command " . (join "-", $command->@*));
    }
}

1;