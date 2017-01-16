package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * DTO for querying the musicbrainz recording API endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicbrainzRecording {

    private Long length;
    private Boolean video;
    private String disambiguation;
    private String title;
    @JsonProperty("id")
    private UUID mbid;


    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getMbid() {
        return mbid;
    }

    public void setMbid(UUID mbid) {
        this.mbid = mbid;
    }
}
