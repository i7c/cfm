package Cfm::Playback::PlaybackRepo;
use strict; use warnings;
use Log::Any qw/$log/;
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;
use Cfm::Common::ListRes;
use Cfm::Playback::Playback;
use DBI qw/:sql_types/;
use JSON::MaybeXS;

has client => singleton 'Cfm::Playback::PlaybackClient';
has config => singleton 'Cfm::Config';
has db => inject 'db';
has kv => singleton 'Cfm::Db::Kv';

sub find {
  my ($self, $page, $broken) = @_;

  if (!$broken && $self->try_use_local()) {
    return $self->find_local($page);
  } else {
    $log->info('Local db not ready, fall back to remote ...');
    return $self->find_remote($page, $broken);
  }
}

sub find_local {
  my ($self, $page) = @_;

  my $data_stm = $self->db->prepare('
    select data
    from playback
    order by timestamp desc
    limit ?
    offset ?
    ');

  my $page_size = 20;
  $data_stm->bind_param(1, $page_size, SQL_BIGINT);
  $data_stm->bind_param(2, $page * $page_size, SQL_BIGINT);
  $data_stm->execute();

  my @res;
  while (my $row = $data_stm->fetch()) {
    push @res, Cfm::Playback::Playback->from_hash(decode_json($row->[0]));
  }

  my $count_stm = $self->db->prepare('
    select count (*)
    from playback
    ');
  $count_stm->execute();
  my $count = $count_stm->fetch()->[0];

  Cfm::Common::ListRes->new(
    elements => [ @res ],
    size => $page_size,
    count => scalar @res,
    number => $page,
    total => $count,
    totalPages => int($count / $page_size) + 1,
  );
}

sub find_remote {
  my ($self, $page, $broken) = @_;

  $self->client->my_playbacks($broken, $page);
}

sub store {
  my ($self, $playback) = @_;

  $self->client->create_playback($playback);
}

sub store_all {
  my ($self, $playbacks) = @_;

  $self->client->batch_create_playbacks(
    Cfm::Playback::PlaybackBatchRes->new(playbacks => $playbacks)
  );
}

# Local db stuff

sub eligible_for_local {
  my ($self) = @_;

  $self->config->get_option('db')
    && (
      time() - ($self->kv->get('last_update') // 0) < $self->config->get_option('sync-interval')
      || ($self->kv->get('last_update') // '') eq '0'
    );
}

sub eligible_for_update {
  my ($self) = @_;

  $self->config->get_option('db');
}

sub try_use_local {
  my ($self) = @_;

  return 1 if $self->eligible_for_local();
  return 0 unless $self->eligible_for_update();
  $self->update_local();
  $self->eligible_for_local();
}

sub update_local {
  my ($self) = @_;

  print STDERR "Sync local database ...\n";
  # TODO: this is obviously bullshit, but we use it for development now ...
  $self->db->do('delete from playback;');
  my $page = 0;
  my $pbs;
  $log->info("Populating local db ...");
  do {
    $pbs = $self->client->my_playbacks(0, $page++, 1000);
    for my $pb ($pbs->elements->@*) {
      my $stm = $self->db->prepare('
        insert into playback (id, timestamp, data)
        VALUES (?, ?, ?);');
      $stm->bind_param(1, $pb->id);
      $stm->bind_param(2, $pb->timestamp, SQL_BIGINT);
      $stm->bind_param(3, encode_json(Cfm::Playback::Playback->to_hash($pb)));
      $stm->execute();
    }
    my $total_pages = $pbs->totalPages;
    $log->info("Chunk $page/$total_pages done");
  } while ($page < ($pbs->totalPages // 0));
  $self->kv->set('last_update', time());
  $log->info("Local db is now up to date");
}

1;
