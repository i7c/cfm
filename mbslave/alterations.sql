create view musicbrainz.artist_ci2 as select a.*, lower(a.name) as lower_name from musicbrainz.artist a;
