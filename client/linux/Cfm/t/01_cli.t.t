use strict;
use warnings FATAL => 'all';
use Test::More tests => 19;
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
