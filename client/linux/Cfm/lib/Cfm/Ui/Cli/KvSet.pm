package Cfm::Ui::Cli::KvSet;
use strict; use warnings;
use Moo;
with 'Cfm::Singleton', 'Cfm::Ui::Cli::Command';

use Cfm::Autowire;

has kv => singleton 'Cfm::Db::Kv';

sub run {
  my ($self, $k, $v) = @_;

  $self->kv->set($k, $v);
}

1;

=head1 NAME

  cfm kv set - Update an entry in the local kv table

=head1 SYNOPSIS

  cfm kv set 'key' 'val'

=head1 DESCRIPTION

  This is an 'internal command' to set or update an entry in the internal kv
  table. Note, that this command accesses the kv table directly, there is no
  semantic sanity checking whatsoever.

  See kv --help for details of respected entries.

=cut
