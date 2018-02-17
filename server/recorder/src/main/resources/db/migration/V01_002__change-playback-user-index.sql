ALTER TABLE ONLY playback
ADD CONSTRAINT ix_playback_user_uuid UNIQUE (user_oid, uuid);

ALTER TABLE ONLY playback
DROP CONSTRAINT IF EXISTS ix_playback_uuid;

