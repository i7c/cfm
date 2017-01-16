package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Represents an artist retrieved from musicbrainz.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbArtistDto {

    private String name;
    private String sortName;
    @JsonProperty("id")
    private UUID mbid;

    public String getName() {
        return name;
    }

    public String getSortName() {
        return sortName;
    }

    public UUID getMbid() {
        return mbid;
    }

}
