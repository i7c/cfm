package Cfm::Client::Client;

use strict;
use warnings;
use Moo;
use Log::Any qw/$log/;

use LWP::UserAgent;
use HTTP::Headers;
use HTTP::Request;
use Carp;
use JSON::MaybeXS;
use Data::Dumper;
use URI;

has url => (is => 'rw');
has auth_user => (is => 'ro');
has auth_pass => (is => 'ro');
has headers => (is => 'rw');
has agent => (is => 'rw');

sub init {
    my ($self, $base_url, $user, $pass) = @_;

    $log->debug("Initialising generic HTTP client");

    $self->url($base_url);

    my $headers = HTTP::Headers->new;
    $headers->content_type("application/json");
    $headers->authorization_basic($user, $pass);
    $self->headers($headers);

    my $ua = LWP::UserAgent->new;
    $self->agent($ua);
}

sub _generic_request {
    my ($self, $method, $path, $content, $params) = @_;

    my $uri = URI->new($self->url);
    $uri->path($path);
    $uri->query_form(@$params);

    my $request = HTTP::Request->new($method, $uri, $self->headers);
    $request->content($content);
    # perform request
    $log->info("Request: $method $uri");
    $log->debug("Request body " . Dumper($content)) if defined $content;
    my $resp = $self->agent->request($request);
    $log->info("Response Code: " . $resp->code);
    if ($resp->is_success) {
        $log->debug("Response: " . $resp->decoded_content);
        return $resp->decoded_content;
    }
    else {
        die $log->error($resp->status_line . ": " . $uri);
    }
}

sub _generic_json_request {
    my ($self, $method, $path, $content, $params) = @_;

    decode_json(
        $self->_generic_request($method, $path, encode_json($content), $params)
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

sub put_json {
    my ($self, $path, $content, $params) = @_;

    $self->_generic_json_request("PUT", $path, $content, $params);
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
