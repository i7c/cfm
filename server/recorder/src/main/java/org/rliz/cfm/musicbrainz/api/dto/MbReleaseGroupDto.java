package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * DTO for release groups from musicbrainz.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbReleaseGroupDto {

    private String title;

    @JsonProperty("id")
    private UUID mbid;

    @JsonProperty("artist-credit")
    @NotNull
    private List<MbArtistCreditDto> artistCredits;

    public String getTitle() {
        return title;
    }

    public UUID getMbid() {
        return mbid;
    }

    public List<MbArtistCreditDto> getArtistCredits() {
        return artistCredits;
    }
}
