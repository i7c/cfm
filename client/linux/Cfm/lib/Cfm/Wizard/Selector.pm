package Cfm::Wizard::Selector;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::SelectorFormatter;

my $logger = Log::Log4perl->get_logger("cfm");

has client => (is => 'ro');

has formatter => (is => 'rw');

sub BUILD {
    my ($self) = @_;

    $self->formatter(Cfm::SelectorFormatter->new);
}

sub _prev_page {
    my ($list) = @_;

    my $page = $list->pageNumber - 1;
    $page = 0 if $page < 0;
    return $page;
}

sub _next_page {
    my ($list) = @_;

    my $page = $list->pageNumber + 1;
    $page = $list->totalPages - 1 if $page >= $list->totalPages;
    return $page;
}

sub select_playback {
    my ($self, $broken) = @_;

    my $page = 0;
    my $last_page = -1;
    my $playbacks;
    my $line;
    while (1) {
        if ($last_page != $page) {
            $logger->info("Get playback page");
            $playbacks = $self->client->my_playbacks($broken, $page);
            $last_page = $page;
        }
        $self->formatter->playback_list($playbacks);
        print "Select playback, (p)revious/(n)ext page, (q)uit: ";
        $line = <STDIN>;
        if ($line =~ /^[pP]$/) {
            $logger->info("Selected previous page");
            $page = _prev_page($playbacks);
        } elsif ($line =~ /^[nN]$/) {
            $logger->info("Selected next page");
            $page = _next_page($playbacks);
        } elsif ($line =~ /^[qQ]$/) {
            print "Cancel\n";
            return undef;
        } elsif ($line =~ /^[0-9]+$/) {
            my ($number) = $line =~ m/([0-9]+)/;
            $logger->debug("Selected playback with index $number");
            if ($number >= 1 && $number <= $playbacks->pageSize) {
                return $playbacks->elements->[$number - 1];
            } else {
                print "Pick number between 1 and " . ($playbacks->pageSize) . "\n";
            }
        }
    }
}

sub select_releasegroup {
    my ($self, $artists, $release) = @_;

    my $page = 0;
    my $last_page = -1;
    my $rgs;
    my $line;
    while (1) {
        if ($last_page != $page) {
            $logger->info("Getting release groups for playback details");
            $logger->debug("Artists: " . join(", ", $artists->@*));
            $logger->debug("Release: " . $release);
            $rgs = $self->client->find_releasegroups($artists, $release, $page);
            $last_page = $page;
            if ($rgs->totalElements < 1) {
                print "No results found.\n";
                return undef;
            }
        }
        $self->formatter->mb_releasegroup_list($rgs);
        print "Select release group, (p)revious/(n)ext page, (q)uit [1]: ";
        $line = <STDIN>;
        if ($line =~ /^[pP]$/) {
            $logger->info("Selected previous page");
            $page = _prev_page($rgs);
        } elsif ($line =~ /^[nN]$/) {
            $logger->info("Selected next page");
            $page = _next_page($rgs);
        } elsif ($line =~ /^[qQ]$/) {
            print "Cancel\n";
            return undef;
        } elsif ($line =~ /^[0-9]*$/) {
            my $number;
            if ($line =~ /^$/) {
                $number = 1;
            } else {
                ($number) = $line =~ m/([0-9]+)/;
            }
            $logger->debug("Selected playback with index $number");
            if ($number >= 1 && $number <= $rgs->pageSize) {
                return $rgs->elements->[$number - 1];
            } else {
                print "Pick number between 1 and " . ($rgs->pageSize) . "\n";
            }
        }
    }
}

sub select_recording {
    my ($self, $rgid, $title) = @_;

    my $page = 0;
    my $last_page = -1;
    my $recs;
    my $line;
    while (1) {
        if ($last_page != $page) {
            $logger->info("Getting recordings for rgid and title");
            $logger->debug("Rgid: $rgid");
            $logger->debug("Title: $title");
            $recs = $self->client->find_recordings($rgid, $title, $page);
            $last_page = $page;
            if ($recs->totalElements < 1) {
                print "No results found.\n";
                return undef;
            }
        }
        $self->formatter->mb_recording_list($recs);
        print "Select recording, (p)revious/(n)ext page, (q)uit [1]: ";
        $line = <STDIN>;
        if ($line =~ /^[pP]$/) {
            $logger->info("Selected previous page");
            $page = _prev_page($recs);
        } elsif ($line =~ /^[nN]$/) {
            $logger->info("Selected next page");
            $page = _next_page($recs);
        } elsif ($line =~ /^[qQ]$/) {
            print "Cancel\n";
            return undef;
        } elsif ($line =~ /^[0-9]*$/) {
            my $number;
            if ($line =~ /^$/) {
                $number = 1;
            } else {
                ($number) = $line =~ m/([0-9]+)/;
            }
            $logger->debug("Selected recording with index $number");
            if ($number >= 1 && $number <= $recs->pageSize) {
                return $recs->elements->[$number - 1];
            } else {
                print "Pick number between 1 and " . ($recs->pageSize) . "\n";
            }
        }
    }
}

1;
