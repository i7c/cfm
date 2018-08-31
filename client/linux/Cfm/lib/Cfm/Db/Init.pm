package Cfm::Db::Init;

use strict; use warnings;

sub setup {
  my ($class, $db) = @_;

  $db->do('
    create table if not exists kv
    (
      k varchar(255),
      v varchar(1023),
      primary key (k)
    );
  ');

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

1;
