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
    BUILD_REQUIRES => {
        'Test::More' => 0,
    },
    PREREQ_PM => {
        #'ABC'              => 1.6,
        #'Foo::Bar::Module' => 5.0401,
    },
    dist  => { COMPRESS => 'gzip -9f', SUFFIX => 'gz', },
    clean => { FILES => 'Cfm-*' },
);
