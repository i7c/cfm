package Cfm::Client;

use Moo;
use LWP::UserAgent;
use HTTP::Headers;
use HTTP::Request;
use Carp;
use JSON;
use Data::Dumper;

use Cfm::Artist;
use Cfm::ArtistList;
use Cfm::Playback;


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

# Perform GET request on given path and return decoded content
sub _plain_get {
    my ($self, $path) = @_;

    my $abspath = $self->cfm_url.$path;
    my $request = HTTP::Request->new('GET', $abspath, $self->headers);
    my $resp = $self->agent->request($request);
    if ($resp->is_success) {
        return $resp->decoded_content;
    } else {
        croak $resp->status_line.": ".$abspath;
    }
}

# Perform GET request on given path and return response as hash
sub _get {
    my ($self, $path) = @_;

    return JSON::decode_json($self->_plain_get($path));
}

# Perform POST request on given path with content and return response
sub _plain_post {
    my ($self, $path, $content) = @_;

    my $abspath = $self->cfm_url.$path;

    my $request = HTTP::Request->new('POST', $abspath, $self->headers);
    $request->content($content);
    my $response = $self->agent->request($request);
    if ($response->is_success) {
        return $response->decoded_content;
    } else {
        croak $response->status_line.": ".$abspath;
    }
}

# Perform POST request on path and return result as hash
sub _post {
    my ($self, $path, $content) = @_;
    my $encoded = JSON::encode_json($content);
    return JSON::decode_json($self->_plain_post($path, $encoded));
}

# Get artists
sub artists {
    my ($self) = @_;

    my $artists_resource = $self->_get("/artists");
    my $result = Cfm::ArtistList->from_hash($artists_resource);
    return $result;
}

# Get single artist
sub artist {
    my ($self, $identifier) = @_;

    my $artist_resource = $self->_get("/artists/$identifier");
    return Cfm::Artist->from_hash($artist_resource);
}

# Create a new playback
sub create_playback {
    my ($self, $create_playback) = @_;

    my $response = $self->_post("/playbacks", $create_playback->dto);
    return Cfm::Playback->from_hash($response);
}

1;
