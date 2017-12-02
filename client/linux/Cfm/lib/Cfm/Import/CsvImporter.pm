package Cfm::Import::CsvImporter;
use strict;
use warnings FATAL => 'all';
use Log::Any qw/$log/;
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;
use Cfm::Config;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackBatchRes;
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

    my $chunk;
    while (($chunk = $self->_read_chunk($fh)) && scalar $chunk->@* > 0) {

        try {
            my $batch = $self->_make_playbacks($chunk);
            my $res = $self->playback_service->batch_create(Cfm::Playback::PlaybackBatchRes->new(playbacks => $batch));
            for (my $i = 0; $i < scalar $chunk->@*; $i++) {
                if ($res->results->[$i]->success) {
                    $count++;
                } else {
                    $errors++;
                    print $report_file $chunk->[$i] if defined $report_file;
                }
            }
        } catch {
            $errors += scalar $chunk->@*;
            print $report_file (join '', $chunk->@*) if defined $report_file;
        };
        print "$count playbacks imported; $errors errors\r";
    }
    print "$count playbacks imported; $errors errors occurred\n";
    print "See the fail log for lines that could not be imported\n" if defined $report_file;
    close $fh;
    close $report_file if defined $report_file;
}

sub _read_chunk {
    my ($self, $fh) = @_;
    my @lines = ();

    while (my $line = <$fh>) {
        push @lines, $line;
        last unless scalar @lines < 25;
    }
    return \@lines;
}

sub _make_playbacks {
    my ($self, $lines) = @_;

    [ map {
        $_ =~ $self->line_pattern;
        Cfm::Playback::Playback->new(
            artists        => [ $+{artist} ],
            recordingTitle => $+{title},
            releaseTitle   => $+{release},
            timestamp      => Time::Piece->strptime($+{timestamp}, $self->date_format)->epoch,
        );
    } $lines->@* ];
}

1;
