package Cfm::Db::Init;

use strict; use warnings;

sub setup {
  my ($class, $db) = @_;

  return unless $class->needs_setup();

}

sub needs_setup {
  my ($class, $db) = @_;

  1;
}

1;
