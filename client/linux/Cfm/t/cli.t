use strict;
use warnings FATAL => 'all';
use Test::More tests => 37;
use Test::Exception;
use Test::MockObject;

use Cfm::Ui::Cli;

sub cut {Cfm::Ui::Cli->new(@_);}

sub mock_config {
    my ($config) = @_;

    Test::MockObject->new()
        ->set_true("add_flags")
        ->mock("get_option", sub {
            my ($self, $o) = @_;
            $config->{$o};
        })
        ->mock("has_option", sub {
            my ($self, $o) = @_;

            defined $config->{$o};
        })
        ->mock("require_option", sub {
            my ($self, $o) = @_;

            die unless defined $config->{$o};
            $config->{$o};
        });
}

# Greedy match should pick the command and leave the args
{
    my ($cmd, $cmdargs) = cut()->greedy_match_command([ qw/list x --y/ ]);
    ok ($cmd eq "list", "list is valid command");
    ok ($cmdargs->[0] eq "x");
    ok ($cmdargs->[1] eq "--y");
}

# Unknown command should die
{
    dies_ok {cut()->greedy_match_command([ "blub" ])} "die with unknown command";
}

# list command with args
{
    my $mock_p = Test::MockObject->new()
        ->mock("my_playbacks", sub {478;});

    my $mock_f = Test::MockObject->new()
        ->set_true("playback_list");

    my $mock_c = mock_config({
        page   => 3,
        broken => 0,
    });

    cut(
        playback_service => $mock_p,
        formatter        => $mock_f,
        config           => $mock_c,
    )->run("list");

    $mock_p->called_pos_ok(1, "my_playbacks", "called my_playbacks on service");
    $mock_p->called_args_pos_is(1, 2, 2, "page arg");
    $mock_p->called_args_pos_is(1, 3, 0, "broken arg");
    $mock_f->called_pos_ok(1, "playback_list", "called playback_list on formatter");
    $mock_f->called_args_pos_is(1, 2, 478, "formatter called with playback list result");
    is ($mock_f->call_pos(2), undef, "no further calls on formatter");
}

# list --acc command
{
    my $mock_p = Test::MockObject->new()
        ->mock("accumulated_broken_playbacks", sub {945612;});

    my $mock_f = Test::MockObject->new()
        ->set_true("accumulated_playbacks");

    my $mock_c = mock_config({
        page => 9,
        acc  => 1,
    });

    cut(
        playback_service => $mock_p,
        formatter        => $mock_f,
        config           => $mock_c,
    )->run("list");

    $mock_p->called_pos_ok(1, "accumulated_broken_playbacks", "called accumulated_broken_playbacks on service");
    $mock_p->called_args_pos_is(1, 2, 8, "page arg");
    $mock_f->called_pos_ok(1, "accumulated_playbacks", "called accumulated_playbacks on formatter");
    $mock_f->called_args_pos_is(1, 2, 945612, "formatter called with accumulated playbacks result");
    is ($mock_f->call_pos(2), undef, "no further calls on formatter");
}

# add command with args
{
    my $mock_pbs = Test::MockObject->new()
        ->mock("create_playback", sub {849;});

    my $mock_f = Test::MockObject->new()
        ->set_true("playback");

    my $mock_c = mock_config()
        ->mock("kv_store", sub {
            + {
                artist   => "A",
                artist2  => "B",
                title    => "T",
                release  => "R",
                playtime => 5,
            }
        });

    cut(playback_service => $mock_pbs,
        formatter        => $mock_f,
        config           => $mock_c,
    )->run("add");

    $mock_pbs->called_pos_ok(0, "create_playback", "called create_playback on service");
    my $actual_pb = $mock_pbs->call_args_pos(0, 2);
    ok ($actual_pb->artists->[0] eq "A", "first artist set");
    ok ($actual_pb->artists->[1] eq "B", "second artist set");
    ok ($actual_pb->recordingTitle eq "T", "recording title");
    ok ($actual_pb->releaseTitle eq "R", "release title");
    ok ($actual_pb->playTime == 5, "play time");

    $mock_f->called_pos_ok(1, "playback", "called playback on formatter");
    $mock_f->called_args_pos_is(1, 2, 849, "formatter called with playback result")
}

# import csv command
{
    my $mock_i = Test::MockObject->new
        ->set_true("import_csv");

    cut(csv_importer => $mock_i,
        config       => mock_config()
    )->run(qw/import csv file1 file2/);

    $mock_i->called_args_pos_is(1, 2, "file1", "passed file1");
    $mock_i->called_args_pos_is(2, 2, "file2", "passed file2");
}

# record mpris command
{
    my $mock_m = Test::MockObject->new
        ->set_true("listen");

    cut(
        mpris2   => $mock_m,
        loglevel => sub {
            ok (1, "loglevel called")
        },
        config   => mock_config({
            player => "xp",
        }),
    )->run(qw/record mpris/);

    $mock_m->called_args_pos_is(1, 2, "xp", "listen() called with player");
}

# now
{
    my $mock_pbs = Test::MockObject->new
        ->mock("get_now_playing", sub {994;});
    my $mock_f = Test::MockObject->new
        ->set_true("playback");

    cut(
        playback_service => $mock_pbs,
        formatter        => $mock_f,
        config           => mock_config(),
    )->run(qw/now/);

    $mock_pbs->called_pos_ok(1, "get_now_playing", "now command asks pbs first");
    $mock_f->called_args_pos_is(1, 2, 994, "formatter gets returned playback");
}

# find rg
{
    my $mock_m = Test::MockObject->new
        ->mock("identify_release_group", sub {31300;});
    my $mock_f = Test::MockObject->new
        ->set_true("release_groups");

    cut(
        mbservice => $mock_m,
        formatter => $mock_f,
        config    => mock_config({
            page => 0,
        }),
    )->run(qw/find rg a b c d/);

    $mock_m->called_pos_ok(1, "identify_release_group", "find-rg calls identify_release_group");
    is ($mock_m->call_args_pos(1, 2)->[0], "b", "first artist");
    is ($mock_m->call_args_pos(1, 2)->[1], "c", "second artist");
    is ($mock_m->call_args_pos(1, 2)->[2], "d", "third artist");
    is ($mock_m->call_args_pos(1, 3), "a", "release group");
    is ($mock_m->call_args_pos(1, 4), - 1, "page");
    $mock_f->called_pos_ok(1, "release_groups", "find-rg calls release_groups formatter");
    $mock_f->called_args_pos_is(1, 2, 31300, "release groups list is passed to formatter");
}
