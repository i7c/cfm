package Cfm::Ui::Cli::Command;
use Moo::Role;

use Pod::Usage qw/pod2usage/;
use Pod::Find qw/pod_where/;
use Pod::Text;
use IO::Pager;

requires qw/run help/;

sub help {
  my ($self) = @_;

  my $pager = IO::Pager->new(\*STDOUT);
  Pod::Text->new()->parse_file(pod_where({-inc => 1}, ref $self));
}

1;
