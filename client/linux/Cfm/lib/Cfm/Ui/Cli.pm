package Cfm::Ui::Cli;
use strict;
use warnings FATAL => 'all';
use Moo;
use Log::Any qw($log);

use Cfm::Autowire;
use Cfm::Config;
use Cfm::Connector::Mpd;
use Cfm::Connector::Mpris2;
use Cfm::Connector::Multi;
use Cfm::Import::CsvImporter;
use Cfm::Mb::MbService;
use Cfm::Playback::Playback;
use Cfm::Playback::PlaybackService;
use Cfm::Stats::StatsService;
use Cfm::Ui::Format::Formatter;
use Cfm::Ui::Wizard::FixAccPlaybackWizard;
use Cfm::Ui::Wizard::SelectPlaybackWizard;
use Cfm::User::User;
use Cfm::User::UserService;

my %command_mapping = (
    'add'          => \&cmd_add,
    'create-user'  => \&cmd_create_user,
    'delete'       => \&cmd_delete,
    'find-rg'      => \&cmd_find_rg,
    'fix'          => \&cmd_fix,
    'fixlog'       => \&cmd_fixlog,
    'import-csv'   => \&cmd_import_csv,
    'list'         => \&cmd_list,
    'now'          => \&cmd_now,
    'record'       => \&cmd_record,
    'record-mpd'   => \&cmd_record_mpd,
    'record-mpris' => \&cmd_record_mpris,
    'recs'         => \&cmd_recs,
    'rgs'          => \&cmd_rgs,
);

has loglevel => inject 'loglevel';
has formatter => inject 'formatter';

has config => singleton 'Cfm::Config';
has csv_importer => singleton 'Cfm::Import::CsvImporter';
has fix_acc_playback_wizard => singleton 'Cfm::Ui::Wizard::FixAccPlaybackWizard';
has mbservice => singleton 'Cfm::Mb::MbService';
has mpd => singleton 'Cfm::Connector::Mpd';
has mpris2 => singleton 'Cfm::Connector::Mpris2';
has multi => singleton 'Cfm::Connector::Multi';
has playback_service => singleton 'Cfm::Playback::PlaybackService';
has select_playback_wizard => singleton 'Cfm::Ui::Wizard::SelectPlaybackWizard';
has stats_service => singleton 'Cfm::Stats::StatsService';
has user_service => singleton 'Cfm::User::UserService';

sub run {
    my ($self, @args) = @_;

    $self->config->add_flags(\@args);
    my ($cmd, $cmdargs) = $self->greedy_match_command(\@args);
    $log->debug("$cmd args: " . join " ", $cmdargs->@*);

    $command_mapping{$cmd}->($self, $cmdargs->@*);
}

sub greedy_match_command {
    my ($self, $command) = @_;
    my $c = scalar($command->@*);
    my $match = "";
    my $match_index = - 1;

    for (my $i = 0; $i < $c; $i++) {
        my $candidate = join "-", @{$command}[0 .. $i];
        if (defined $command_mapping{$candidate}) {
            $match_index = $i;
            $match = $candidate;
        }
    }
    if ($match_index >= 0) {
        return ($match, [ @{$command}[$match_index + 1 .. $c - 1] ])
    } else {
        die $log->error("No such command " . (join "-", $command->@*));
    }
}

### Commands ###

sub cmd_list {
    my ($self) = @_;

    # if the user specifies --acc, we use the service method to get accumulated broken playbacks, rather than the
    # regular list. --broken then has no effect on the result.
    if ($self->config->has_option('acc')) {
        my $acc_playbacks = $self->playback_service->accumulated_broken_playbacks($self->config->get_option('page') - 1);
        $self->formatter->accumulated_playbacks($acc_playbacks);
    } else {
        my $playbacks = $self->playback_service->my_playbacks($self->config->get_option('page') - 1,
            $self->config->get_option("broken"));
        $self->formatter->playback_list($playbacks, $self->config->get_option("verbose"));
    }
}

sub cmd_add {
    my ($self) = @_;

    my $kv = $self->config->kv_store();
    my $playback = Cfm::Playback::Playback->new(
        artists        => [ $kv->{artist}, $kv->{artist1}, $kv->{artist2}, $kv->{artist3} ],
        recordingTitle => $kv->{title},
        releaseTitle   => $kv->{release},
        timestamp      => $kv->{timestamp},
        playTime       => $kv->{playtime},
        trackLength    => $kv->{length},
        discNumber     => $kv->{disc},
        trackNumber    => $kv->{track},
        id             => $kv->{id},
        source         => $kv->{source},
    );
    my $response = $self->playback_service->create_playback($playback);
    $self->formatter->playback($response);
}

sub cmd_delete {
    my ($self) = @_;

    my $source = $self->config->require_option("source");
    my $affected = $self->playback_service->delete($source);
    $self->formatter->affected($affected);
}

sub cmd_import_csv {
    my ($self, @args) = @_;

    for my $file (@args) {
        $log->info("Importing playbacks from $file");
        $self->csv_importer->import_csv($file);
    }
}
sub cmd_record {
    my ($self) = @_;

    $self->loglevel->("info") unless $self->config->has_option("quiet");
    $self->multi->listen();
}

sub cmd_record_mpris {
    my ($self) = @_;

    my $player = $self->config->require_option("player");
    $self->loglevel->("info") unless $self->config->has_option("quiet");
    $self->mpris2->listen($player);
}

sub cmd_record_mpd {
    my ($self) = @_;

    $self->loglevel->("info") unless $self->config->has_option("quiet");
    $self->mpd->listen();
}

sub cmd_create_user {
    my ($self) = @_;

    my $kv = $self->config->kv_store();
    my $state;
    if (defined $kv->{state} && $kv->{state} eq "active") {
        $state = $Cfm::User::User::active;
    } else {
        $state = $Cfm::User::User::inactive;
    }
    my $user = Cfm::User::User->new(
        name       => $kv->{name} // $kv->{user},
        password   => $kv->{password} // $kv->{pass},
        state      => $state,
        systemUser => 0,
    );
    my $response = $self->user_service->create_user($user);
    $self->formatter->user($response);
}

sub cmd_now {
    my ($self) = @_;

    my $np = $self->playback_service->get_now_playing;
    $self->formatter->playback($np);
}

sub cmd_find_rg {
    my ($self, $rg, @artists) = @_;

    my $rgs = $self->mbservice->identify_release_group(\@artists, $rg, $self->config->get_option("page") - 1);
    $self->formatter->release_groups($rgs);
}

sub cmd_fix {
    my ($self) = @_;

    while (1) {
        my $acc = $self->select_playback_wizard->select_accumulated_broken;
        return unless defined $acc;
        $self->fix_acc_playback_wizard->run($acc);
    }
}

sub cmd_fixlog {
    my ($self) = @_;

    my $fixlog = $self->playback_service->fixlog(
        $self->config->get_option("page") - 1
    );
    $self->formatter->playback_list($fixlog, $self->config->get_option("verbose"), 1);
}

sub cmd_rgs {
    my ($self) = @_;

    $self->formatter->show(
        $self->stats_service->top_release_groups
    );
}

sub cmd_recs {
    my ($self) = @_;

    $self->formatter->show(
        $self->stats_service->top_recordings
    );
}

1;
