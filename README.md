# cfm
cfm is a free and open source listening/playback tracker designed in client /
("micro")service architecture. It currently consists of a text-based Perl client
and two Spring Boot services that both build on top of a PostgreSQL database.

Some features:
* A text-based Perl client, implemented to be scriptable and yet somewhat
  user-friendly

* Extensive musicbrainz integration. All recorded (played) tracks are looked up
  on the musicbrainz database and associated with musicbrainz identifiers. This
  way, we can compare listening behaviour even across service boundaries.

* The back-end can be run with plain old Java SE or simply be deployed on Amazon
  AWS, Cloudfoundry, ... without any manual effort. Deploying the services does
  not require any advanced knowledge.

## Back-end Services

Currently, cfm consists of two back-end services
* recorder: this service provides the REST interface to the client implementing
  all the basic operations that the client exposes to the user.

* mbs: the "musicbrainz service" provides a REST API to the recorder service in
  order to query and search the musicbrainz database. This service is currently
  not meant to be exposed to the public. There is no authentication
  architecture.

Furthermore, cfm contains a set of somewhat stupid scripts to build a
replication-ready musicbrainz database clone on a AWS EC2 machine without much
effort. This script set can be found in the mbslave directory.
