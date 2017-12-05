use strict;
use warnings FATAL => 'all';
use Test::More tests => 13;
use Test::Exception;
use Test::MockObject;

use Cfm::Config;

my $conf = Cfm::Config->new(
    conf => {
        abc => 1,
        x   => "value"
    }
);

# get_option()
{
    ok ($conf->get_option("x") eq "value", "get_option()")
}

# has_option()
{
    ok ($conf->has_option("x"), "has_option()")
}

# require_option() happy case
{
    ok ($conf->require_option("abc"), "require_option()")
}

# require_option() sad
{
    dies_ok {$conf->require_option("blub")} "require_option() fails missing option"
}

# add_flags() adds options
{
    my $conf = Cfm::Config->new(
        conf => {
            x => "y"
        }
    );

    $conf->add_flags([ qw/--page 3/ ]);

    ok ($conf->get_option("x") eq "y", "unshadowed option remains");
    ok ($conf->get_option("page") == 3, "flag creates option");
}

# add_flags() replaces existing options
{
    my $conf = Cfm::Config->new(
        conf => {
            page => 1
        }
    );

    $conf->add_flags([ qw/--page 2/ ]);

    ok ($conf->get_option("page") == 2, "flag replaces option");
}

# add_flags() shadows default options
{
    my $conf = Cfm::Config->new();

    ok ($conf->get_option("page") == 0, "empty config returns default");

    $conf->add_flags([ qw/--page 2/ ]);

    ok ($conf->get_option("page") == 2, "flag shadows default option");
}

# kv_store()
{
    my $conf = Cfm::Config->new(
        conf => {
            set => [
                " x : y",
                "a:b",
                "c: 3"
            ]
        }
    );

    my $kv = $conf->kv_store();

    ok ($kv->{x} eq "y", "kv_store with spaces");
    ok ($kv->{a} eq "b", "kv_store without spaces");
    ok ($kv->{c} == 3, "kv_store with number");
    ok (!defined $kv->{blub}, "unspecified is undefined");
}

1;
