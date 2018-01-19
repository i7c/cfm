package Cfm::Ui::Selector::Selector;
use strict;
use warnings FATAL => 'all';

use Term::Form;

sub numerical_select {
    my ($data_cb, $show_data_cb, $extract_elements_cb) = @_;

    $extract_elements_cb //= sub {$_[0]->elements};
    my $page = 0;
    my $form = Term::Form->new("numerical_select");

    my $data = $data_cb->($page);
    while (1) {
        print "\n";
        $show_data_cb->($data);
        my $elements = $extract_elements_cb->($data);
        my $count = scalar $elements->@*;
        my $selection = $form->readline("Choose [1..$count/p/n/x/q]: ");

        return undef unless defined $selection;
        if ($selection =~ /\s*n\s*/) {
            $page++;
            $data = $data_cb->($page);
            next;
        } elsif ($selection =~ /\s*p\s*/) {
            $page--;
            $data = $data_cb->($page);
            next;
        } elsif ($selection =~ /\s*q\s*/) {
            exit 1;
        } elsif ($selection =~ /^\s*\d+\s*$/) {
            next if $selection < 1 || $selection > scalar $elements->@*;
            return $elements->[int($selection) - 1];
        } elsif ($selection =~ /\s*x\s*/) {
            die;
        }
    };
}

sub yes_no {
    my ($question) = @_;

    my $form = Term::Form->new("yes_no");
    my $selection = $form->readline("$question [y/n]: ");

    return undef unless defined $selection;
    return ($selection =~ /\s*[yY]\s*/)
}

1;
