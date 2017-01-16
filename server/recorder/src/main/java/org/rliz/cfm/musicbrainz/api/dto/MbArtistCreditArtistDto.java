package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Represents the reference to an artist within the artist credits.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbArtistCreditArtistDto {

    @JsonProperty("id")
    @NotNull
    private UUID mbid;

    public UUID getMbid() {
        return mbid;
    }
}
