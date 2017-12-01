package Cfm::Import::CsvImporter;
use strict;
use warnings FATAL => 'all';
use Log::Any qw/$log/;
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;
use Cfm::Config;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackService;
use Time::Piece;
use Try::Tiny;

my %format_patterns = (
    # https://benjaminbenben.com/lastfm-to-csv/
    ben => qr/^(?<artist>[^,]*),
        (?<release>[^,]*),
        (?<title>[^,]*),
        (?<timestamp>[^,]*[^,\s]+)\s*$/x,
);

my %date_patterns = (
    # https://benjaminbenben.com/lastfm-to-csv/
    ben => "%d %b %Y %H:%M",
);

has line_pattern => (
        is      => 'lazy',
        default => sub {$format_patterns{$_[0]->config->get_option("csv-format")};},
    );
has date_format => (
        is      => 'lazy',
        default => sub {$date_patterns{$_[0]->config->get_option("date-format")};},
    );

has config => singleton "Cfm::Config";
has playback_service => singleton "Cfm::Playback::PlaybackService";

sub import_csv {
    my ($self, $filename) = @_;

    my $count = 0;
    my $errors = 0;
    my $report_file = undef;

    open(my $fh, '<:encoding(UTF-8)', $filename) or die $log->fatal("Cannot read file $filename");
    if ($self->config->has_option("fail-log")) {
        open($report_file, '>:encoding(UTF-8)', $self->config->get_option("fail-log"));
    }
    STDOUT->autoflush(1);

    while (my $line = <$fh>) {
        $line =~ $self->line_pattern;

        my $playback = Cfm::Playback::Playback->new(
            artists        => [ $+{artist} ],
            recordingTitle => $+{title},
            releaseTitle   => $+{release},
            timestamp      => Time::Piece->strptime($+{timestamp}, $self->date_format)->epoch,
        );

        try {
            $self->playback_service->create_playback($playback);
            $count++;
        } catch {
            print $report_file $line if defined $report_file;
            $errors++;
        };
        print "$count playbacks imported; $errors errors\r";
    }
    print "$count playbacks imported; $errors errors occurred\n";
    print "See the fail log for lines that could not be imported\n" if defined $report_file;
    close $fh;
    close $report_file if defined $report_file;
}

1;
