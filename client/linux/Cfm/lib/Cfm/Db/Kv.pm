package Cfm::Db::Kv;
use strict; use warnings;
use Moo;
with 'Cfm::Singleton';

use Cfm::Autowire;

has db => inject 'db';

sub get {
  my ($self, $k) = @_;

  my $stm = $self->db->prepare('
    select v
    from kv
    where k = ?');
  $stm->execute($k);
  my $v = $stm->fetch();
  return undef unless defined $v;
  $v->[0];
}

sub set {
  my ($self, $k, $v) = @_;

  my $stm = $self->db->do('insert or replace into kv (k, v) VALUES(?, ?)',
    undef, $k, $v);
}

sub unset {
  my ($self, $k) = @_;

  $self->db->do('delete from kv where k = ?', undef, $k);
}

sub all {
  my ($self) = @_;

  my $stm = $self->db->prepare('select k, v from kv');
  $stm->execute();
  my %kv;
  while (my $row = $stm->fetch()) {
    $kv{$row->[0]} = $row->[1];
  }
  \%kv;
}

1;
