package Cfm::Ui::Cli::List;
use strict; use warnings;
use Moo;
with 'Cfm::Singleton', 'Cfm::Ui::Cli::Command';

use Cfm::Autowire;

has config => singleton 'Cfm::Config';
has formatter => inject 'formatter';
has playback_service => singleton 'Cfm::Playback::PlaybackService';

sub run {
    my ($self) = @_;

    my $playbacks = $self->playback_service->my_playbacks(
        $self->config->get_option('page') - 1,
        $self->config->get_option('broken'),
        $self->config->get_option('amount'),
    );
    $self->formatter->playback_list(
        $playbacks,
        $self->config->get_option("verbose"),
    );
}

1;

=head1 NAME

    cfm list - Show overview of existing playbacks

=head1 SYNOPSIS

    cfm list [--page <n>|-p <n>] [--broken] [--format <format>] [--verbose|-v]

=head1 DESCRIPTION

    Lists playbacks in the order of occurrence, that is the most recent
    playback is on the very top.

=head1 OPTIONS

    --amount <n>, -n <n>
        Request n elements instead of the default amount. When requesting from
        the local store, the amount is unlimited. When requesting remotely, a
        maximum of 200 will be forced.

        You can specify 0 to request all elements, if the local store is used.

    --page <n>, -p <n>
        Show the n-th page of playbacks instead of the first one. We start
        counting at 1.

    --broken
        Only list playbacks that are broken, that is, playbacks that have no
        associated musicbrainz entity. You should attempt to keep the number of
        broken playbacks low, because they can lead to inaccurate statistics.
        Some statistics may interpret them "as good as possible" and some stats
        might simply ignore broken playbacks.

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

    --verbose, -v
        This only applies to the default "pretty" format. It adds more columns
        to display, if they are not already present in the 'list-cols'.
        Currently, it is the "Id" column that is added. This might be useful if
        you want to refer to a specific playback.

=cut
