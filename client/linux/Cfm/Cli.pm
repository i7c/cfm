package Cfm::Cli;
use strict;
use warnings FATAL => 'all';
use Getopt::Long;
use Moo;
use Carp;

my %command_mapping = (
    help   => \&cmd_help,
    artist => \&cmd_artist
);

has 'client' => (is => 'ro');

has 'options' => (is => 'rw');

sub BUILD {
    Getopt::Long::Configure ("bundling");
}

sub run {
    my ($self) = @_;

    return $self->cmd_help unless @ARGV;
    my $command = shift @ARGV if $ARGV[0];
    if ($command_mapping{$command}) {
        my %options = ();
        GetOptions(
            \%options,
            'artistid|aid=s',
            'derp=s'
        );
        $self->options(\%options);
        $command_mapping{$command}->($self);
    } else {
        print "Unknown command $command.";
    }
}

# Gets an option from the options map
sub get_option {
    my ($self, $option) = @_;

    return $self->options->{$option};
}

# Returns the option if set and fails with error message otherwise
sub require_option {
    my ($self, $option) = @_;

    my $result = $self->get_option($option);
    croak "You must provide the $option option." unless $result;
    return $result;
}

sub cmd_help {
    print "Help.";
}

sub cmd_artist {
    my ($self) = @_;

}

1;