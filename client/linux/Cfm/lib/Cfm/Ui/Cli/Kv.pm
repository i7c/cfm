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

=head2 Respected values

  The following is a potentially complete list of the values in the kv table
  and their meaning.

  last_update
    If unset, cfm assumes there is no information about the last sync and will
    most likely attempt to trigger one, if the local store is needed.

    If set to 0, cfm will consider the local state to be up-to-date and usable,
    independent of how much time has passed since the last sync. This is *not*
    the same as an 'offline mode'. It merely skips up-to-date checking.

    If set to t > 0, cfm will consider t to be the last point in time when the
    local database was updated. Depending on the update policy, it depends on
    the configuration options and the difference (now() - t) if an automatic
    update will be triggered.

    Usually, a sync will update last_update's value accordingly.

  offline
    If set to a true value, cfm will forcefully prevent all communication with
    the server. cfm will exit immediately, if any command attempts to contact
    the server.

    Note, that this might behave unexpectedly, if set manually. That is, some
    commands might misbehave when offline is true and other options are not set
    accordingly.

    The offline enty is mostly meant for development purposes and it is
    probably smart, not to set it. It might be removed in the future.

  version
    Version of the local store. This has nothing to do with the cfm client
    version. This option makes sure that cfm does not read or write
    incompatible versions of the data store.

=cut
