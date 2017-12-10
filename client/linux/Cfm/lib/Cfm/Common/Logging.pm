package Cfm::Common::Logging;
use strict;
use warnings FATAL => 'all';
use Log::Log4perl;
use Log::Log4perl::Layout;
use Log::Log4perl::Level;
use Log::Any::Adapter;

use Cfm::Bean;

my %levels = (
    debug => $DEBUG,
    info  => $INFO,
    warn  => $WARN,
    error => $ERROR,
);

sub setup_logger {
    my $logger = Log::Log4perl->get_logger("");
    my $layout = Log::Log4perl::Layout::PatternLayout->new("%d %10r [%c] %L %m%n");
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
        my $l = $ENV{CFM_LL};

        $logger->level($levels{$l}) if defined $levels{$l};
        # If the log level is set by an environment variable, we do not allow any code to reset the level again. Thus
        # we register a sub that does nothing as bean.
        bean loglevel => sub {sub {1;}};
    } else {
        $logger->level($WARN); # default level
        # We register a closure as bean, which can be used to reset the log level. We could as well put the logger
        # directly into the bean, but that would expose logging implementation details to the client code. The sub
        # abstracts the details of the logging implementation. Whenever logging is setup, one can inject loglevel
        # anywhere and call the sub.
        bean loglevel => sub {
                sub {
                    my ($l) = @_;

                    $logger->level($levels{$l}) if defined $levels{$l};
                }
            };
    }
    Log::Any::Adapter->set('Log4perl');
    $logger->debug("Log initialisation completed");

    return $logger;
}

1;
