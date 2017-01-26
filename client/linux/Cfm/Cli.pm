package Cfm::Cli;
use strict;
use warnings FATAL => 'all';
use Getopt::Long;
use Moo;
use Carp;

use Cfm::Client;
use Cfm::Playback;
use Cfm::PrettyFormatter;

my %command_mapping = (
    help     => \&cmd_help,
    artists  => \&cmd_artists,
    playback => \&cmd_playback
);

has 'client' => (is => 'rw');

has 'options' => (is => 'rw');

has 'formatter' => (is => 'rw');

sub BUILD {
    Getopt::Long::Configure ("bundling");
}

sub run {
    my ($self) = @_;
    return $self->cmd_help unless @ARGV;
    my $command = shift @ARGV if $ARGV[0];
    if ($command_mapping{$command}) {
        my %options = ();
        GetOptions(
            \%options,
            "cfm-url|h=s",
            "cfm-user|u=s",
            "cfm-password|p=s",
            "mb-track=s",
            "mb-release-group=s",
            "quiet"
        );
        $self->options(\%options);
        $self->set_client;
        $self->set_formatter;
        $command_mapping{$command}->($self);
    } else {
        print "Unknown command $command.";
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

    return $self->options->{$option};
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

sub cmd_help {
    print "Help.";
}

sub cmd_artists {
    my ($self) = @_;

    my $artist_list = $self->client->artists;
    $self->formatter->artist_list($artist_list);
    return;
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
        carp "Specify --mb-track and --mb-album."
    }
}

1;