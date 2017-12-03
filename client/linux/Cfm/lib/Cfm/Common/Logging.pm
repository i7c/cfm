package Cfm::Common::Logging;
use strict;
use warnings FATAL => 'all';
use Log::Log4perl;
use Log::Log4perl::Layout;
use Log::Log4perl::Level;
use Log::Any::Adapter;

sub setup_logger {
    my $logger = Log::Log4perl->get_logger("");
    my $layout = Log::Log4perl::Layout::PatternLayout->new("%d %5r [%c] %L %m%n");
    my $stdout_appender;
    # use colors if stderr is a terminal
    if (-t STDERR) {
        $stdout_appender = Log::Log4perl::Appender->new(
            "Log::Log4perl::Appender::ScreenColoredLevels",
            name   => "screenlog",
            stderr => 1);
    } else {
        $stdout_appender = Log::Log4perl::Appender->new(
            "Log::Log4perl::Appender::Screen",
            name   => "screenlog",
            stderr => 1);
    }
    $stdout_appender->layout($layout);
    $logger->add_appender($stdout_appender);
    # environment variable CFM_LL can be used to reset log level before bootstrapping
    if (defined $ENV{CFM_LL}) {
        my $level = $ENV{CFM_LL};
        if ($level eq "info") {
            $logger->level($INFO);
        } elsif ($level eq "debug") {
            $logger->level($DEBUG);
        } elsif ($level eq "error") {
            $logger->level($ERROR);
        } elsif ($level eq "warn") {
            $logger->level($WARN);
        }
    } else {
        $logger->level($WARN); # default level
    }
    Log::Any::Adapter->set('Log4perl');
    $logger->debug("Log initialisation completed");
    return $logger;
}

1;
