#!/bin/env perl
use warnings FATAL => 'all';
use strict;
use Log::Log4perl;
use Log::Log4perl::Layout;
use Log::Log4perl::Level;

use Cfm::Cli;

my $logger = Log::Log4perl->get_logger("cfm");
my $layout = Log::Log4perl::Layout::PatternLayout->new("%d %r [%c] %C %L %m%n");
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
# environment varible CFM_LOG_LEVEL can be used to
if (defined $ENV{CFM_LOG_LEVEL}) {
    my $level = $ENV{CFM_LOG_LEVEL};
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

$logger->info("Log initialisation completed");
$logger->info('
 ______  ______ __    __
/\  ___\/\  ___/\ "-./  \
\ \ \___\ \  __\ \ \-./\ \
 \ \_____\ \_\  \ \_\ \ \_\
  \/_____/\/_/   \/_/  \/_/  (c) Constantin Weisser, 2017

'
);
$logger->info("Starting CLI ...");
my $cli = Cfm::Cli->new;
$cli->run;
$logger->info("Application is terminating normally.");
exit 0;