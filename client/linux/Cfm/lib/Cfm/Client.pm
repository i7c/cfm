package Cfm::Client;

use Moo;
use LWP::UserAgent;
use HTTP::Headers;
use HTTP::Request;
use Carp;
use JSON;
use Data::Dumper;
use URI;

use Cfm::Artist;
use Cfm::ArtistList;
use Cfm::Playback;
use Cfm::PlaybackList;


# URL of the back-end server
has cfm_url => (is => 'ro');

# Username
has cfm_user => (is => 'ro');

# Password
has cfm_password => (is => 'ro');

# Default headers
has headers => (is => 'rw');

# HTTP user agent
has agent => (is => 'rw');

# Setup client
sub BUILD {
    my ($self, $args) = @_;
    my $headers = HTTP::Headers->new;
    if ($args->{cfm_user} && $args->{cfm_password}) {
        $headers->authorization_basic($args->{cfm_user},
            $args->{cfm_password});
    }
    $headers->content_type("application/json");
    my $ua = LWP::UserAgent->new(default_headers => $headers);
    $self->agent($ua);
    $self->headers($headers);
}

sub _generic_request {
    my ($self, $method, $path, $content, $params) = @_;

    # set request path
    my $uri = URI->new($self->cfm_url);
    $uri->path($path);
    # set query params
    $uri->query_form(@$params);

    # build request
    my $request = HTTP::Request->new($method, $uri, $self->headers);
    # set body
    $request->content($content);
    # perform request
    my $resp = $self->agent->request($request);
    if ($resp->is_success) {
        return $resp->decoded_content;
    } else {
        croak $resp->status_line.": ".$uri;
    }
}

# Perform GET request on given path and return decoded content
sub _plain_get {
    my ($self, $path, $params) = @_;
    return $self->_generic_request('GET', $path, \{}, $params);
}

# Perform GET request on given path and return response as hash
sub _get {
    my ($self, $path, $params) = @_;

    return JSON::decode_json($self->_plain_get($path, $params));
}

# Perform POST request on given path with content and return response
sub _plain_post {
    my ($self, $path, $content, $params) = @_;
    $self->_generic_request('POST', $path, $content, $params);
}

# Perform POST request on path and return result as hash
sub _post {
    my ($self, $path, $content, $params) = @_;
    my $encoded = JSON::encode_json($content);
    return JSON::decode_json($self->_plain_post($path, $encoded, $params));
}

sub _plain_delete {
    my ($self, $path, $content, $params) = @_;
    $self->_generic_request('DELETE', $path, $content, $params);
}

sub _delete {
    my ($self, $path, $content, $params) = @_;
    my $encoded = JSON::encode_json($content);
    return JSON::decode_json($self->_plain_delete($path, $encoded, $params));
}

# Get artists
sub artists {
    my ($self) = @_;

    my $artists_resource = $self->_get("/api/v1/artists");
    my $result = Cfm::ArtistList->from_hash($artists_resource);
    return $result;
}

# Get single artist
sub artist {
    my ($self, $identifier) = @_;

    my $artist_resource = $self->_get("/api/v1/artists/$identifier");
    return Cfm::Artist->from_hash($artist_resource);
}

# Create a new playback
sub create_playback {
    my ($self, $create_playback) = @_;

    my $response = $self->_post("/api/v1/playbacks", $create_playback->dto);
    return Cfm::Playback->from_hash($response);
}

# Get playback list
sub my_playbacks {
    my ($self, $only_broken) = @_;

    my @params = ();
    if ($only_broken) {
        push @params, onlyBroken => "true";
    }

    my $response = $self->_get("/api/v1/playbacks/mine", \@params);
    return  Cfm::PlaybackList->from_hash($response);
}

1;
