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
has cfm_url => (
    is => 'ro'
);

# Username
has cfm_user => (
    is => 'ro'
);

# Password
has cfm_password => (
    is => 'ro'
);

# Default headers
has headers => (
    is => 'rw'
);

# HTTP user agent
has agent => (
    is => 'rw'
);

sub BUILD {
    my ($self, $args) = @_;
    my $headers = HTTP::Headers->new;
    if ($args->{cfm_user} && $args->{cfm_password}) {
        $headers->authorization_basic($args->{cfm_user},
            $args->{cfm_password});
    }
    my $ua = LWP::UserAgent->new(default_headers => $headers);
    $self->agent($ua);
    $self->headers($headers);
}


sub _plain_get() {
    my $self = shift;
    my $path = shift;

    my $abspath = $self->cfm_url . $path;
    my $resp = $self->agent->get($abspath);
    if ($resp->is_success) {
        return $resp->decoded_content;
    } else {
        croak $resp->status_line . ": " . $abspath;
    }
}

sub _json_get() {
    my $self = shift;
    my $path = shift;

    return JSON::decode_json($self->_plain_get($path));
}

sub _plain_post() {
    my ($self, $path, $content) = @_;

    my $abspath = $self->cfm_url . $path;

    my $request = HTTP::Request->new('POST', $abspath, $self->headers);
    $request->header("Content-Type" => "application/json");
    $request->content($content);
    my $response = $self->agent->request($request);
    if ($response->is_success) {
        return $response->decoded_content;
    } else {
        croak $response->status_line . ": " . $abspath;
    }
}

sub _json_post() {
    my ($self, $path, $content) = @_;
    my $encoded = JSON::encode_json($content);
    return JSON::decode_json($self->_plain_post($path, $encoded));
}

sub artists() {
    my $self = shift;

    my $artists = $self->_json_get("/artists");
    my $result = Cfm::ArtistList->from_hash($artists);
    return $result;
}

sub playback() {
    my ($self, $create_playback)  = @_;

    my $response = $self->_json_post("/playbacks", $create_playback->dto);
    return Cfm::Playback->from_hash($response);
}

1;
