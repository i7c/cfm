package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Represents the nested structure of artist credits in other responses.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbArtistCreditDto {

    @NotNull
    private MbArtistCreditArtistDto artist;

    public MbArtistCreditArtistDto getArtist() {
        return artist;
    }
}
