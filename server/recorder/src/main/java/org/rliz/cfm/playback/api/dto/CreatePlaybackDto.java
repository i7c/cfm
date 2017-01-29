package org.rliz.cfm.playback.api.dto;

import java.util.UUID;

/**
 * DTO that represents creation of a playback.
 */
public class CreatePlaybackDto  {

    private UUID mbTrackId;

    private UUID mbReleaseGroupId;

    private String artist;

    private String title;

    private String album;

    public UUID getMbTrackId() {
        return mbTrackId;
    }

    public UUID getMbReleaseGroupId() {
        return mbReleaseGroupId;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }
}
