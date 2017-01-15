package org.rliz.cfm.artist.api.dto;

import java.util.UUID;

/**
 * DTO to create an artist.
 */
public class CreateArtistDto {

    private String name;

    private UUID mbid;

    public String getName() {
        return name;
    }

    public UUID getMbid() {
        return mbid;
    }
}
