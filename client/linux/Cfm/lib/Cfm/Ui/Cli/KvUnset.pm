package Cfm::Ui::Cli::KvUnset;
use strict; use warnings;
use Moo;
with 'Cfm::Singleton', 'Cfm::Ui::Cli::Command';

use Cfm::Autowire;

has kv => singleton 'Cfm::Db::Kv';

sub run {
  my ($self, $k) = @_;

  die "See --help for usage" unless defined $k;
  $self->kv->unset($k);
}

1;

=head1 NAME

  cfm kv unset - Delete an entry in the kv table.

=head1 SYNOPSIS

  cfm kv unset 'key'

=head1 DESCRIPTION

  This is an 'internal command' to delete an entry in the internal kv table.

=cut
