package Cfm::Client;

use Moo;
use LWP::UserAgent;
use HTTP::Headers;
use Carp;
use JSON;

use Cfm::Artist;
use Cfm::ArtistList;

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

sub BUILD {
    my ($self, $args) = @_;
    my $headers = HTTP::Headers->new;
    if ($args->{cfm_user} && $args->{cfm_password}) {
        $headers->authorization_basic($args->{cfm_user},
            $args->{cfm_password});
    }
    my $ua = LWP::UserAgent->new(default_headers => $headers);
    $self->{"ua"} = $ua;
}


sub _plain_get() {
    my $self = shift;
    my $path = shift;

    my $ua = $self->{ua};
    my $abspath = $self->{cfm_url} . $path;
    my $resp = $ua->get($abspath);
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

sub artists() {
    my $self = shift;

    my $artists = $self->_json_get("/artists");
    my $result = Cfm::ArtistList->from_hash($artists);
    return $result;
}

1;
