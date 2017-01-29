package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Represents a release in the musicbrainz API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbReleaseDto {

    @JsonProperty("id")
    private UUID mbid;

    private String title;

    @JsonProperty("release-group")
    private MbReleaseGroupReferenceDto releaseGroupReference;

    @JsonProperty("track-count")
    private Integer trackCount;

    @JsonProperty("track-offset")
    private Integer trackOffset;

    public UUID getMbid() {
        return mbid;
    }

    public String getTitle() {
        return title;
    }

    public MbReleaseGroupReferenceDto getReleaseGroupReference() {
        return releaseGroupReference;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public Integer getTrackOffset() {
        return trackOffset;
    }
}
