package org.rliz.cfm.musicbrainz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * A reference to a release group in the musicbrainz API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MbReleaseGroupReferenceDto {

    @JsonProperty("id")
    private UUID mbid;

    public UUID getMbid() {
        return mbid;
    }
}
