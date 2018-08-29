package Cfm::Ui::Cli::Now;
use strict; use warnings;
use Moo;
with 'Cfm::Singleton', 'Cfm::Ui::Cli::Command';

use Cfm::Autowire;

has config => singleton 'Cfm::Config';
has formatter => inject 'formatter';
has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub run {
    my ($self) = @_;

    my $np = $self->playback_service->get_now_playing;
    $self->formatter->show($np);
}

1;

=head1 NAME

    cfm now - Show currently playing track

=head1 SYNOPSIS

    cfm now [--format <format>]

=head1 DESCRIPTION

    Shows the currently (or most recently) playing track as playback. The
    presented object has all properties of a regular playback. It can be
    "absent", i.e. non-existent. In this case, the "now" command will print
    nothing at all.

    The ID is a bit special, as it never changes and is identical to your user's
    technical ID - for now. This might be subject to change in the future.

    Note that the playback's time might be in the future. Clients can (and the
    cfm record command does!) put the playback's timestamp to the best estimate
    that the real persisted playback will have. I.e. if you start listening to a
    track of 1 minute length at time t, the "now" playback will show a timestamp
    of t+60, which is somewhere close to the real playback's timestamp, that
    will be created later.

    The "now" playback is not a real playback, i.e. commands like "list" won't
    show this playback at all. It is also not included in any stats. Clients can
    set the "now" playback when a new track starts, and create a real playback
    once the track is finished.

=head1 OPTIONS

    --format <format>
        You can specify an alternative format for displaying playbacks.
        Currently, there are three options available: pretty, json, csv. The
        default is pretty.

        pretty
            Prints tables with help of ascii chars. You should never parse this
            format. You can configure the displayed columns with the list-cols
            option.

        json
            Prints the data in json format. cfm will output nothing else and
            thus you should be able to parse the output with a regular json
            parser.

        csv
            Produces the data in CSV format. Note, the limitation: In CSV
            format, cfm will only provide the *first* artist (whichever that is)
            instead of listing all artists. This is due to the fact, that we
            don't want to nest CSV within CSV and neither do we want variable
            amount of columns.

=cut
