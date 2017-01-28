package Cfm::Cli;
use strict;
use warnings FATAL => 'all';
use Getopt::Long;
use Moo;
use Carp;
use Config::Simple;

use Cfm::Client;
use Cfm::Playback;
use Cfm::PrettyFormatter;

my %command_mapping = (
    artists  => \&cmd_artists,
    playback => \&cmd_playback
);

my %help_mapping = (
    artists  => \&help_artists,
    playback => \&help_playback
);

my @config_locations = (
    $ENV{'HOME'} . "/.cfm.conf",
    $ENV{'HOME'} . "/.config/cfm/config",
    "/etc/cfm.conf"
);

my %conf_default = (

);

has 'client' => (is => 'rw');

has 'options' => (is => 'rw');

has 'formatter' => (is => 'rw');

has 'conf' => (is => 'rw');

sub BUILD {
    Getopt::Long::Configure ("bundling");
}

sub run {
    my ($self) = @_;
    my $command = shift @ARGV if $ARGV[0];
    my %options = ();
    GetOptions(
        \%options,
        "cfm-url|h=s",
        "cfm-user|u=s",
        "cfm-password|p=s",
        "mb-track=s",
        "mb-release-group=s",
        "quiet|q",
        "help|h"
    );
    $self->options(\%options, $command);
    $self->load_config;
    return if $self->handle_help($command);
    $self->set_client;
    $self->set_formatter;
    $command_mapping{$command}->($self);
}

sub handle_help {
    my ($self, $command) = @_;

    if ($self->has_option("help")) {
        $help_mapping{$command}->($self);
        return 1;
    } elsif (!$command) {
        print "Available Commands:\n";
        for my $available_command (keys %command_mapping) {
            print "    $available_command\n";
        }
        return 1;
    } else {
        return 0;
    }
}

sub load_config {
    my ($self) = @_;

    for my $conf_file (@config_locations) {
        if (-f $conf_file) {
            my $conf = Config::Simple->new($conf_file);
            $self->conf($conf);
            return;
        }
    }
}

sub set_client {
    my ($self) = @_;

    my $url = $self->require_option("cfm-url");
    my $user = $self->require_option("cfm-user");
    my $pw = $self->require_option("cfm-password");

    $self->client(Cfm::Client->new(
        cfm_url      => $url,
        cfm_user     => $user,
        cfm_password => $pw)
    );
}

sub set_formatter {
    my ($self) = @_;

    if ($self->has_option("pretty")) {
        $self->formatter(Cfm::PrettyFormatter->new());
        return;
    }
    # Fallback:
    $self->formatter(Cfm::PrettyFormatter->new());
}

# Gets an option from the options map
sub get_option {
    my ($self, $option) = @_;

    my $cliopt = $self->options->{$option};
    return $cliopt if $cliopt;
    return $self->conf()->param($option) if $self->conf && $self->conf->param($option);
    return $conf_default{$option};
}

# Returns the option if set and fails with error message otherwise
sub require_option {
    my ($self, $option) = @_;

    my $result = $self->get_option($option);
    croak "You must provide the $option option." unless $result;
    return $result;
}

sub has_option {
    my ($self, $option) = @_;
    return 1 if $self->get_option($option);
    return 0;
}

sub cmd_artists {
    my ($self) = @_;

    my $artist_list = $self->client->artists;
    $self->formatter->artist_list($artist_list);
    return;
}

sub help_artists {
    print "Usage: artists\n";
}

sub cmd_playback {
    my ($self) = @_;

    my $mb_track_id = $self->get_option("mb-track");
    my $mb_release_group_id = $self->get_option("mb-release-group");

    if ($mb_track_id && $mb_release_group_id) {
        my $create_playback = Cfm::CreatePlayback->new(
            mbTrackId        => $mb_track_id,
            mbReleaseGroupId => $mb_release_group_id
        );
        my $playback = $self->client->create_playback($create_playback);
        if (!$self->has_option("quiet")) {
            $self->formatter->playback($playback);
        }
        return;
    } else {
        carp "Specify --mb-track and --mb-release-group."
    }
}

sub help_playback {
    print 'Usage: playback [options]

Creates a new playback on the server. If successful, the playback is returned.

Options:
    --mb-track           the musicbrainz id of the track
    --mb-release-group   the musicbrainz id of the release group
    --quiet              do not return anything in case of success
';
}

1;