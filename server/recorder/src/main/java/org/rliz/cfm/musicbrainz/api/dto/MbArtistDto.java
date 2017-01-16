package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Represents an artist retrieved from musicbrainz.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbArtistDto {

    @NotNull
    private String name;

    @NotNull
    private String sortName;

    @JsonProperty("id")
    @NotNull
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