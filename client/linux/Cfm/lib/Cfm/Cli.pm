package Cfm::Cli;
use strict;
use warnings FATAL => 'all';
use Getopt::Long;
use Moo;
use Carp;
use Config::Simple;
use Log::Log4perl;
use Log::Log4perl::Level;
use Data::Dumper;

use Cfm::Client;
use Cfm::Playback;
use Cfm::PrettyFormatter;
use Cfm::Mpris2Connector;
use Cfm::SavePlaybackDto;

my %command_mapping = (
    artists   => \&cmd_artists,
    playback  => \&cmd_playback,
    playbacks => \&cmd_playbacks,
    record    => \&cmd_record,
    del       => \&cmd_del,
    fix       => \&cmd_fix,
);

my %help_mapping = (
    artists   => \&help_artists,
    playback  => \&help_playback,
    playbacks => \&help_playbacks,
    record    => \&help_record,
    del       => \&help_del,
    fix       => \&help_fix,
);

my @config_locations = (
    $ENV{'HOME'} . "/.cfm.conf",
    $ENV{'HOME'} . "/.config/cfm/config",
    "/etc/cfm.conf"
);

my %conf_default = (

);

my $logger = Log::Log4perl->get_logger("cfm");

has 'client' => (is => 'rw');

has 'options' => (is => 'rw');

has 'formatter' => (is => 'rw');

has 'conf' => (is => 'rw');

sub BUILD {
    Getopt::Long::Configure ("bundling");
}

sub run {
    my ($self) = @_;
    my %options = ();

    $logger->debug("Parse command line options ...");
    GetOptions(
        \%options,
        "cfm-url|h=s",
        "cfm-user|u=s",
        "cfm-password|p=s",
        "mb-track=s",
        "mb-release-group=s",
        "quiet|q",
        "help|h",
        "artist|a=s",
        "title|t=s",
        "album|A=s",
        "player=s",
        "broken",
        "id|i=s",
        "debug",
        "log|L=s"
    );
    $self->options(\%options);
    $self->set_log_level;
    $logger->debug("Parsed options: " . Dumper(\%options));
    $logger->debug("Remaining input: " . join(" ", @ARGV));
    $self->load_config;
    return if $self->handle_help; # if help was requested, exit afterwards
    $self->set_client;
    $self->set_formatter;
    $self->handle_command;
}

sub set_log_level {
    my ($self) = @_;

    my $level = $self->get_option("log");
    if (defined $level) {
        if ($level eq "info") {
            $logger->level($INFO);
            $logger->info("Reset log level to INFO");
            return;
        } elsif ($level eq "debug") {
            $logger->level($DEBUG);
            $logger->info("Reset log level to DEBUG");
            return;
        } elsif ($level eq "warn") {
            $logger->level($WARN);
            $logger->info("Reset log level to WARN");
            return;
        } elsif ($level eq "error") {
            $logger->level($ERROR);
            $logger->info("Reset log level to ERROR");
            return;
        } else {
            $logger->warn("Unknown log level $level. Ignoring.");
        }
    }
}

sub handle_help {
    my ($self) = @_;

    if (!defined $ARGV[0]) {
        print "Specify one of the following commands:\n";
        for my $available_command (keys %command_mapping) {
            print "    $available_command\n";
        }
        print "Help is available using the --help option.\n";
    }

    my $command = $ARGV[0];
    if ($self->has_option("help")) {
        if (defined $help_mapping{$command}) {
            $help_mapping{$command}->($self);
            return 1;
        } else {
            $logger->error("No help available for $command");
            die;
        }
    } else {
        return 0;
    }
}

sub handle_command {
    my ($self) = @_;

    my $command = $ARGV[0];
    if (defined $command_mapping{$command}) {
        $command_mapping{$command}->($self);
    } else {
        $logger->error("Unknown command $command");
        die;
    }
}

sub load_config {
    my ($self) = @_;

    for my $conf_file (@config_locations) {
        $logger->debug("Try location " . $conf_file);
        if (-f $conf_file) {
            $logger->info("Found config file: $conf_file");
            my $conf = Config::Simple->new($conf_file);
            $self->conf($conf);
            return;
        }
    }
    $logger->info("Ran out of config file locations. Proceed without configuration file.");
}

sub set_client {
    my ($self) = @_;

    my $url = $self->require_option("cfm-url");
    my $user = $self->require_option("cfm-user");
    my $pw = $self->require_option("cfm-password");

    $logger->info("Initialise cfm client ...");
    $logger->debug("cfm endpoint: $url");
    $logger->debug("cfm user: $user");
    $logger->debug("cfm password is set") if defined $pw;
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
    if (!defined $result) {
        $logger->error("You must provide the $option option.");
        die;
    }
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
    my $artist = $self->get_option("artist");
    my $title = $self->get_option("title");
    my $album = $self->get_option("album");

    if (($mb_track_id && $mb_release_group_id) || ($artist && $title && $album)) {
        my $create_playback = Cfm::SavePlaybackDto->new(
            mbTrackId        => $mb_track_id,
            mbReleaseGroupId => $mb_release_group_id,
            artist           => $artist,
            title            => $title,
            album            => $album
        );
        my $playback = $self->client->create_playback($create_playback);
        if (!$self->has_option("quiet")) {
            $self->formatter->playback($playback);
        }
        return;
    } else {
        $logger->error("Specify (--mb-track and --mb-release-group) or (--artist and --title and --album).");
        die;
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

sub cmd_playbacks {
    my ($self) = @_;

    my $pbl = $self->client->my_playbacks($self->has_option("broken"));
    $self->formatter->playback_list($pbl);
    return;
}

sub help_playbacks {
    print 'Prints your playbacks.

    Usage: playbacks
';
}

sub cmd_record {
    my ($self) = @_;

    my $player = $self->require_option("player");
    if ($player eq "spotify") {
        my $connector = Cfm::Mpris2Connector->new(
            client    => $self->client,
            dbus_name => "org.mpris.MediaPlayer2.spotify",
            debug     => $self->has_option("debug")
        );
        $logger->info("Initialising MPRIS2 Connector for player $player ...");
        $connector->listen;
    } else {
        $logger->error("Unknown player $player");
        die;
    }

}

sub help_record {
    print 'Starts a process that will record tracks played on one of the supported players.

Usage: record --player=<player>

Supported players:
  spotify
';
}

sub cmd_del {
    my ($self) = @_;

    my $uuid = $self->require_option("id");
    $self->client->delete_playback($uuid);
}

sub help_del {
    print 'Delete a playback.

Usage: del -i <uuid>';
}

sub cmd_fix {
    my ($self) = @_;

    my $uuid = $self->require_option("id");
    my $create_playback = Cfm::SavePlaybackDto->new(
        mbTrackId        => $self->require_option("mb-track"),
        mbReleaseGroupId => $self->require_option("mb-release-group")
    );
    my $updated_playback = $self->client->fix_playback($uuid, $create_playback);
    $self->formatter->playback($updated_playback);
    return;
}

1;