package Cfm::Db::Init;

use strict; use warnings;

sub setup {
  my ($class, $db) = @_;

  return unless $class->needs_setup();

  $db->do('
    create table if not exists kv
    (
      k varchar(255),
      v varchar(1023),
      primary key (k)
    );
  ');
}

sub needs_setup {
  my ($class, $db) = @_;

  1;
}

1;
