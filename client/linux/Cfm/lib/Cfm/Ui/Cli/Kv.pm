package Cfm::Ui::Cli::Kv;
use strict; use warnings;
use Moo;
with 'Cfm::Singleton', 'Cfm::Ui::Cli::Command';

use Cfm::Autowire;

has kv => singleton 'Cfm::Db::Kv';
has formatter => inject 'formatter';

sub run {
  my ($self) = @_;

  $self->formatter->kv($self->kv->all);
}

1;

=head1 NAME

  cfm kv - Examine the internal key-value table

=head1 SYNOPSIS

  cfm kv [--format <format>]

=head1 DESCRIPTION

  More recent versions of cfm maintain local state on the machine to provide a
  better user experience. To store meta data and details about the 'sync state',
  cfm has a simple key-value table that can map strings to strings. The kv
  command can be used to examine this internal table.

  With the commands kv-set and kv-unset you can alter the state of the table.
  Read the respective documentation for details.

  The --format option applies as usual.

=cut
