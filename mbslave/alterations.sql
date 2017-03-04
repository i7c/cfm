create materialized view musicbrainz.artist_ci as select a.*, lower(a.name) as lower_name from musicbrainz.artist a;
