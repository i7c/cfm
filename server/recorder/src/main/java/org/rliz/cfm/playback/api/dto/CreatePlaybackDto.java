package org.rliz.cfm.playback.api.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO that represents creation of a playback.
 */
public class CreatePlaybackDto {

    private UUID mbTrackId;

    private UUID mbReleaseGroupId;

    private List<String> artists;

    private String title;

    private String album;

    private Long length;

    private Integer discNumber;

    private Integer trackNumber;

    private Long playTime;

    public UUID getMbTrackId() {
        return mbTrackId;
    }

    public UUID getMbReleaseGroupId() {
        return mbReleaseGroupId;
    }

    public List<String> getArtists() {
        return artists;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public Long getLength() {
        return length;
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public Long getPlayTime() {
        return playTime;
    }
}
