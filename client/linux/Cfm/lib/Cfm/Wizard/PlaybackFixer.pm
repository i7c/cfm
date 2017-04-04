package Cfm::Wizard::PlaybackFixer;
use strict;
use warnings FATAL => 'all';
use Moo;

use Cfm::Wizard::Selector;
use Cfm::PrettyFormatter;

my $logger = Log::Log4perl->get_logger("cfm");

has client => (is => 'ro');

has selector => (is => 'rw');

has formatter => (is => 'rw');

sub BUILD {
    my ($self) = @_;

    $self->selector(Cfm::Wizard::Selector->new(client => $self->client));
    $self->formatter(Cfm::PrettyFormatter->new);
}

sub run {
    my ($self, $broken) = @_;

    $logger->info("Start selector for playbacks");
    my ($playback, undef) = $self->selector->select_playback($broken);
    if (!defined $playback) {
        print "No playback selected.\n";
        return undef;
    }
    print "Selected Playback: " . $playback->identifier . "\n";
    my $rg = $self->selector->select_releasegroup($playback->originalArtists, $playback->originalAlbum);
    if (!defined $rg) {
        print "No release group selected.\n";
        return undef;
    }
    print "Selected release group: " . $rg->name . "\n";
    my $rec = $self->selector->select_recording($rg->identifier, $playback->originalTitle);
    if (!defined $rec) {
        print "No recording selected.\n";
        return undef;
    }
    print "Selected recording: " . $rec->name . "\n\n";
    $logger->info("Try to fix playback on the server");
    my $dto = Cfm::SavePlaybackDto->new(mbReleaseGroupId => $rg->identifier, mbTrackId => $rec->identifier);
    my $fixed_pb = $self->client->fix_playback($playback->identifier, $dto, 0);
    $self->formatter->playback($fixed_pb);
    return;
}

1;