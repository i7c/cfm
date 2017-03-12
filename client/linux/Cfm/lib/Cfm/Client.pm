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

my $logger = Log::Log4perl->get_logger("cfm");

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
    $logger->info("Request: $method $uri");
    $logger->debug("Request body " . Dumper($content)) if defined $content;
    my $resp = $self->agent->request($request);
    $logger->info("Response Code: " . $resp->code);
    if ($resp->is_success) {
        $logger->debug("Response: " . $resp->decoded_content);
        return $resp->decoded_content;
    } else {
        $logger->error($resp->status_line . ": " . $uri);
        die;
    }
}

# Perform GET request on given path and return decoded content
sub _plain_get {
    my ($self, $path, $params) = @_;
    return $self->_generic_request('GET', $path, \{ }, $params);
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

sub _plain_patch {
    my ($self, $path, $content, $params) = @_;
    $self->_generic_request('PATCH', $path, $content, $params);
}

sub _patch {
    my ($self, $path, $content, $params) = @_;
    my $encoded = JSON::encode_json($content);
    return JSON::decode_json($self->_plain_patch($path, $encoded, $params));
}

# Get artists
sub artists {
    my ($self, $page) = @_;

    my @params = ();
    if ($page > 0) {
        push @params, page => $page;
    }
    my $artists_resource = $self->_get("/api/v1/artists", \@params);
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

    my $response = $self->_post("/api/v1/playbacks", $create_playback->create_dto);
    return Cfm::Playback->from_hash($response);
}

# Get playback list
sub my_playbacks {
    my ($self, $only_broken, $page) = @_;

    my @params = ();
    if ($only_broken) {
        push @params, onlyBroken => "true";
    }
    if ($page > 0) {
        push @params, page => $page;
    }

    my $response = $self->_get("/api/v1/playbacks/mine", \@params);
    return  Cfm::PlaybackList->from_hash($response);
}

# Delete a playback by UUID
sub delete_playback {
    my ($self, $uuid) = @_;

    $self->_plain_delete("/api/v1/playbacks/$uuid");
}

sub fix_playback {
    my ($self, $uuid, $create_playback) = @_;

    my $playback = $self->_patch("/api/v1/playbacks/$uuid", $create_playback->_to_hash);
    return Cfm::Playback->from_hash($playback);
}

1;
