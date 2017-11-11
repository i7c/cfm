use 5.24.0;
use strict;
use warnings;
use ExtUtils::MakeMaker;

WriteMakefile(
    NAME             => 'Cfm',
    AUTHOR           => q{Constantin Weißer <i7c@posteo.de>},
    VERSION_FROM     => 'lib/Cfm.pm',
    ABSTRACT_FROM    => 'lib/Cfm.pm',
    LICENSE          => 'GPLv3',
    PL_FILES         => {},
    MIN_PERL_VERSION => 5.24.0,
    CONFIGURE_REQUIRES => {
        'ExtUtils::MakeMaker' => 0,
    },
    EXE_FILES       => [ qw(script/cfm) ],
    BUILD_REQUIRES => {
        'Test::More' => 0,
    },
    PREREQ_PM => {
        "JSON"                  => "2.94",
        "Carp"                  => "1.38",
        "Config::Simple"        => "4.58",
        "Data::Printer"         => "0.40",
        "Date::Format"          => "2.24",
        "Getopt::Long"          => "2.50",
        "HTTP::Headers"         => "6.13",
        "HTTP::Request"         => "6.13",
        "Log::Log4perl"         => "1.49",
        "LWP::UserAgent"        => "6.26",
        "Moo"                   => "2.003002",
        "Net::DBus"             => "1.1.0",
        "Text::ASCIITable"      => "0.22",
        "Time::HiRes"           => "1.9741",
        "URI"                   => "1.72",
    },
    dist  => { COMPRESS => 'gzip -9f', SUFFIX => 'gz', },
    clean => { FILES => 'Cfm-*' },
);
