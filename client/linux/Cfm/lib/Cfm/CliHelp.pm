package Cfm::CliHelp;
use strict;
use warnings FATAL => 'all';
use Log::Log4perl;

my $logger = Log::Log4perl->get_logger("cfm");

my %help_mapping = (
    artists      => \&help_artists,
    playback     => \&help_playback,
    list         => \&help_list,
    record       => \&help_record,
    del          => \&help_del,
    fix          => \&help_fix,
    "mbfind-rg"  => \&help_mbfind_rg,
    "mbfind-rec" => \&help_mbfind_rec,
);

sub show_help {
    my ($command) = @_;

    if (defined $help_mapping{$command}) {
        $help_mapping{$command}->();
        return 1;
    } else {
        $logger->error("No help available for $command");
        die;
    }
}

sub help_artists {
    print 'Lists the known artists

Usage: artists

Options:
    --page | -P <n>     Request n-th page
';
}

sub help_playback {
    print 'Usage: playback [options]

Creates a new playback on the server. If successful, the playback is returned.
Either (--mb-track and --mb-release-group) or (--artist, --title and --album) must be specified.
--artist must be specified at least once and can be specified multiple times for multiple artists.

Options:
    --mb-track           the musicbrainz id of the track
    --mb-release-group   the musicbrainz id of the release group
    --artist | -a        an artist of the recording, can be specified multiple times
    --title | -t         the title of the recording
    --album | -A         the album name on which the recording appears
    --quiet              do not return anything in case of success
';
}

sub help_list {
    print 'Prints the list of playbacks.

Usage: list

Options:
    --page | -P <n>     Request n-th page
    --broken            Only show "broken" playbacks
';
}

sub help_record {
    print 'Starts a process that will record tracks played on one of the supported players.

Usage: record --player=<player>

Supported players:
  spotify
';
}

sub help_del {
    print 'Delete a playback.

Usage: del -i <uuid>';
}

sub help_mbfind_rg {
    print 'Lookup release groups in the musicbrainz database

Usage: mbfind-rg -a \'My Artist\' -t \'Their Aweseome Release\'

Options:
    --artist | -a <artist name>
    --title | -t <title of the release>
    --page | -P <page number>
';
}

sub help_mbfind_rec {
    print 'Lookup recordings on a specific release group in the musicbrainz database

    Usage: mbfind-rec --rgid <uuid> -t \'Song title\'

Options:
    --rgid <release group id>
    --title | -t <title of the recording>
';
}

1;