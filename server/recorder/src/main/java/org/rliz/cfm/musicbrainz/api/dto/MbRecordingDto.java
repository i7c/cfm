package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/**
 * DTO for querying the musicbrainz recording API endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbRecordingDto {

    private Long length;
    private Boolean video;
    private String disambiguation;
    private String title;
    @JsonProperty("id")
    private UUID mbid;
    @JsonProperty("artist-credits")
    private List<MbArtistCreditsDto> artistCredits;


    public Long getLength() {
        return length;
    }

    public Boolean getVideo() {
        return video;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public String getTitle() {
        return title;
    }

    public UUID getMbid() {
        return mbid;
    }

    public List<MbArtistCreditsDto> getArtistCredits() {
        return artistCredits;
    }
}
