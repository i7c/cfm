package Cfm::Db::Init;
use Log::Any qw($log);

use strict; use warnings;

sub setup {
  my ($class, $db) = @_;

  return if $class->actual_db_version($db) == 1;
  $log->info("Initialising local store ...");

  $db->do('
    create table if not exists kv
    (
      k varchar(255),
      v varchar(1023),
      primary key (k)
    );
  ');
  $db->do('insert into kv (k, v) values("version", "1")');

  $db->do('
    create table if not exists playback
    (
      id varchar(255),
      timestamp bigint,
      data varchar(8191),
      primary key (id)
    );
  ');
}

sub actual_db_version {
  my ($class, $db) = @_;

  my $metastm = $db->prepare('
    select count(*)
    from sqlite_master
    where
      type = "table"
      and tbl_name = "kv";
    ');
  $metastm->execute();
  return 0 if $metastm->fetch()->[0] eq 0;

  my $stm = $db->prepare('select v from kv where k = "version";');
  $stm->execute();
  my $row = $stm->fetch();
  return 0 unless defined $row;
  my $dbversion = $row->[0];
  $dbversion;
}

1;
