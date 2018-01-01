package Cfm::Ui::Format::Formatter;
use strict;
use warnings FATAL => 'all';
use Moo::Role;
with 'Cfm::Singleton';
use Log::Any qw/$log/;

use Cfm::Config;
use Cfm::Ui::Format::Csv;
use Cfm::Ui::Format::Pretty;
use Cfm::Ui::Format::Json;

my %formatters = (
    pretty => sub {Cfm::Ui::Format::Pretty->instance},
    csv    => sub {Cfm::Ui::Format::Csv->instance},
    json   => sub {Cfm::Ui::Format::Json->instance},
);

sub instance {
    my ($class) = @_;

    return $Cfm::Ui::Format::Formatter::singleton //= choose_instance();
}

sub choose_instance {
    my $choice = Cfm::Config->instance->get_option("format");
    $log->info("Choose format $choice");
    return $formatters{$choice}->() if defined $formatters{$choice};
    die $log->error("Unknown format.");
}

sub playback_list {}
sub playback {}
sub user {}
sub accumulated_playbacks {}

1;
