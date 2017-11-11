package Cfm::Client::Client;

use strict;
use warnings;
use Moo;
use Log::Log4perl;

use LWP::UserAgent;
use HTTP::Headers;
use HTTP::Request;
use Carp;
use JSON;
use Data::Dumper;
use URI;

my $logger = Log::Log4perl->get_logger("cfm");

has url => (is => 'ro');
has auth_user => (is => 'ro');
has auth_pass => (is => 'ro');
has headers => (is => 'rw');
has agent => (is => 'rw');

sub BUILD {
    my ($self, $args) = @_;

    $logger->debug("Initialising generic HTTP client");
    my $headers = HTTP::Headers->new;
    if (defined $args->{auth_user} && defined $args->{auth_pass}) {
        $logger->debug("Auth header will be set with user " . $args->{auth_user});
        $headers->authorization_basic($args->{auth_user}, $args->{auth_pass});
    }
    $headers->content_type("application/json");
    my $ua = LWP::UserAgent->new(default_headers => $headers);
    $self->agent($ua);
    $self->headers($headers);
}

sub _generic_request {
    my ($self, $method, $path, $content, $params) = @_;

    my $uri = URI->new($self->url);
    $uri->path($path);
    $uri->query_form(@$params);

    my $request = HTTP::Request->new($method, $uri, $self->headers);
    $request->content($content);
    # perform request
    $logger->info("Request: $method $uri");
    $logger->debug("Request body " . Dumper($content)) if defined $content;
    my $resp = $self->agent->request($request);
    $logger->info("Response Code: " . $resp->code);
    if ($resp->is_success) {
        $logger->debug("Response: " . $resp->decoded_content);
        return $resp->decoded_content;
    }
    else {
        #$logger->error($resp->status_line . ": " . $uri);
        die;
    }
}

sub _generic_json_request {
    my ($self, $method, $path, $content, $params) = @_;

    JSON::decode_json(
        $self->_generic_request($method, $path, JSON::encode_json($content), $params)
    );
}

sub get_json {
    my ($self, $path, $params) = @_;

    $self->_generic_json_request("GET", $path, + {}, $params);
}

sub post_json {
    my ($self, $path, $content, $params) = @_;

    $self->_generic_json_request("POST", $path, $content, $params);
}

sub patch_json {
    my ($self, $path, $content, $params) = @_;

    $self->_generic_json_request("PATCH", $path, $content, $params);
}

sub delete_json {
    my ($self, $path, $params) = @_;

    $self->_generic_json_request("DELETE", $path, + {}, $params);
}

1;
