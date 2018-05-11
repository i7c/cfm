alter table playback
    add raw_artist jsonb,
    add raw_release varchar(768),
    add raw_recording varchar(768),
    add raw_length bigint;

update playback
set
    raw_artist = raw.artist_json::jsonb,
    raw_release = raw.release_title,
    raw_recording = raw.recording_title,
    raw_length = raw.length
from raw_playback_data raw
where playback.original_data_oid = raw.oid;

alter table playback
    alter column raw_artist set not null,
    alter column raw_release set not null,
    alter column raw_recording set not null;
