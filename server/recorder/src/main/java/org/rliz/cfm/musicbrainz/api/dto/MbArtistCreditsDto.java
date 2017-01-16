package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Represents the nested structure of artist credits in other responses.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbArtistCreditsDto {

    @JsonProperty("artist.id")
    private UUID mbid;

    public UUID getMbid() {
        return mbid;
    }
}
