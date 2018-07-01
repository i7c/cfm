alter table playback
    add raw_artists jsonb,
    add raw_release varchar(768),
    add raw_recording varchar(768),
    add raw_length bigint;

update playback
set
    raw_artists = raw.artist_json::jsonb,
    raw_release = raw.release_title,
    raw_recording = raw.recording_title,
    raw_length = raw.length
from raw_playback_data raw
where playback.original_data_oid = raw.oid;

alter table playback
    alter column raw_artists set not null,
    alter column raw_release set not null,
    alter column raw_recording set not null;

create sequence sq_playback owned by playback.oid;
alter table playback alter COLUMN oid set default nextval('sq_playback'::regclass);
select setval('sq_playback'::regclass, (select max(oid) from playback) + 1);
