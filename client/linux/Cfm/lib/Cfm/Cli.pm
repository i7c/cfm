package Cfm::Cli;
use strict;
use warnings FATAL => 'all';
use Getopt::Long;
use Moo;
use Config::Simple;
use Log::Log4perl;
use Log::Log4perl::Level;
use Data::Dumper;

use Cfm::Playback;
use Cfm::PrettyFormatter;
use Cfm::SavePlaybackDto;

my %command_mapping = (
    artists      => \&cmd_artists,
    playback     => \&cmd_playback,
    list         => \&cmd_list,
    record       => \&cmd_record,
    del          => \&cmd_del,
    fix          => \&cmd_fix,
    "mbfind-rg"  => \&cmd_mbfind_rg,
    "mbfind-rec" => \&cmd_mbfind_rec,
    invite       => \&cmd_invite,
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
        "artist|a=s@",
        "title|t=s",
        "album|A=s",
        "player=s",
        "broken",
        "id|i=s",
        "debug",
        "log|L=s",
        "page|P=n",
        "rgid=s",
        "interactive|i",
        "auto",
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
        for my $available_command (sort keys %command_mapping) {
            print " * $available_command\n";
        }
        print "Help is available using the --help option.\n";
        return 1;
    }

    my $command = $ARGV[0];
    if ($self->has_option("help")) {
        require Cfm::CliHelp;
        return Cfm::CliHelp::show_help($command);
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

    require Cfm::Client;
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

sub require_arg {
    my ($self, $n, $name) = @_;

    if (!defined $ARGV[$n]) {
        $logger->error("Missing argument '$name'.");
        exit 1;
    }
    return $ARGV[$n];
}

sub cmd_artists {
    my ($self) = @_;

    my $page = $self->get_option("page") // 1;
    if ($page < 1) {
        $logger->error("--page must be >= 1.");
        die;
    }
    my $artist_list = $self->client->artists($page - 1);
    $self->formatter->artist_list($artist_list);
    return;
}

sub cmd_playback {
    my ($self) = @_;

    my $mb_track_id = $self->get_option("mb-track");
    my $mb_release_group_id = $self->get_option("mb-release-group");
    my $artists = $self->get_option("artist");
    my $title = $self->get_option("title");
    my $album = $self->get_option("album");

    if ((defined $mb_track_id && defined $mb_release_group_id)
        || (defined $artists && defined $title && defined $album)) {
        my $create_playback = Cfm::SavePlaybackDto->new(
            mbTrackId        => $mb_track_id,
            mbReleaseGroupId => $mb_release_group_id,
            artists          => $artists,
            title            => $title,
            album            => $album
        );
        $logger->debug("SavePlaybackDto: " . Dumper($create_playback->_to_hash));
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

sub cmd_list {
    my ($self) = @_;

    my $page = $self->get_option("page") // 1;
    if ($page < 1) {
        $logger->error("--page must be >= 1.");
        die;
    }
    my $pbl = $self->client->my_playbacks($self->has_option("broken"), $page - 1);
    $self->formatter->playback_list($pbl);
    return;
}

sub cmd_record {
    my ($self) = @_;

    my $player = $self->require_option("player");
    if ($player eq "spotify") {
        require Cfm::Connector::Spotify;
        my $connector = Cfm::Connector::Spotify->new(
            client    => $self->client,
            dbus_name => "org.mpris.MediaPlayer2.spotify",
        );
        $logger->info("Initialising Spotify Connector ...");
        $connector->listen;
    } elsif ($player =~ /mpris2:\w+/) {
        require Cfm::Connector::Mpris2;
        my ($mpris2_name) = $player =~ m/mpris2:(.*)/;
        $logger->info("Mpris2 DBus name: $mpris2_name");
        my $connector = Cfm::Connector::Mpris2->new(
            client    => $self->client,
            dbus_name => "org.mpris.MediaPlayer2.$mpris2_name",
        );
        $logger->info("Initialising generic mpris2 connector for player $mpris2_name");
        $connector->listen;
    } else {
        $logger->error("Unknown player $player");
        die;
    }

}

sub cmd_del {
    my ($self) = @_;

    if ($self->has_option("interactive")) {
        require Cfm::Wizard::Selector;
        my $selector = Cfm::Wizard::Selector->new(client => $self->client);
        my $page = 0;
        my $pb;
        while (1) {
            print "Select playback to delete ...\n";
            ($pb, $page) = $selector->select_playback($self->has_option("broken"), $page);
            return if (!defined $pb);
            $self->client->delete_playback($pb->identifier);
        }
    }

    my $uuid = $self->require_arg(1, "identifier");
    $self->client->delete_playback($uuid);
}

sub cmd_fix {
    my ($self) = @_;

    if ($self->has_option("interactive")) {
        $logger->info("Fix in interactive mode, load playback fixer wizard");
        require Cfm::Wizard::PlaybackFixer;
        my $fixer = Cfm::Wizard::PlaybackFixer->new(client => $self->client);
        $logger->info("Running wizard ...");
        $fixer->run($self->has_option("broken"));
        return;
    }
    my $uuid = $self->require_arg(1, "identifier");
    my $auto = $self->has_option("auto");

    #TODO: require mutually exclusivity of --auto and --set in the future
    my $updated_playback;
    if ($auto) {
        $updated_playback = $self->client->fix_playback($uuid, \{ }, 1);
    } else {
        my $create_playback = Cfm::SavePlaybackDto->new(
            mbTrackId        => $self->require_option("mb-track"),
            mbReleaseGroupId => $self->require_option("mb-release-group")
        );
        $updated_playback = $self->client->fix_playback($uuid, $create_playback, 0);
    }
    $self->formatter->playback($updated_playback);
    return;
}

sub cmd_mbfind_rg {
    my ($self) = @_;

    my $artists = $self->require_option("artist");
    my $title = $self->require_option("title");
    my $page = $self->get_option("page") // 1;
    if ($page < 1) {
        $logger->error("--page must be >= 1.");
        die;
    }

    my $release_groups = $self->client->find_releasegroups($artists, $title, $page - 1);
    $self->formatter->mb_releasegroup_list($release_groups);
    return;
}

sub cmd_mbfind_rec {
    my ($self) = @_;

    my $rgid = $self->require_option("rgid");
    my $title = $self->require_option("title");
    my $page = $self->get_option("page") // 1;
    if ($page < 1) {
        $logger->error("--page must be >= 1.");
        die;
    }

    my $recordings = $self->client->find_recordings($rgid, $title, $page - 1);
    $self->formatter->mb_recording_list($recordings);
}

sub cmd_invite {
    my ($self) = @_;

    my $invite = $self->client->create_invite();
    $self->formatter->invite($invite);
}

1;
