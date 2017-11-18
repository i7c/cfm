package Cfm::Ui::Format::Formatter;
use strict;
use warnings FATAL => 'all';
use Moo::Role;
with 'Cfm::Singleton';

use Cfm::Ui::Format::Pretty;

sub instance {
    my ($class) = @_;

    return $Cfm::Ui::Format::Formatter::singleton //= choose_instance();
}

sub choose_instance {
    return Cfm::Ui::Format::Pretty->instance;
}

sub playback_list {}
sub playback {}

1;
