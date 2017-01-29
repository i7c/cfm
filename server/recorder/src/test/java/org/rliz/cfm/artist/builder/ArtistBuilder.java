package org.rliz.cfm.artist.builder;

import org.rliz.cfm.artist.model.Artist;
import org.springframework.security.access.method.P;

import java.util.UUID;

/**
 * Builder class for {@link Artist}s.
 */
public class ArtistBuilder {

    private String name;
    private UUID mbid;
    private UUID identifier;

    private ArtistBuilder() {}

    public static ArtistBuilder artist() {
        return new ArtistBuilder();
    }

    public ArtistBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ArtistBuilder withMbid(UUID mbid) {
        this.mbid = mbid;
        return this;
    }

    public ArtistBuilder withIdentifier(UUID identifier) {
        this.identifier = identifier;
        return this;
    }

    public Artist build() {
        Artist artist = new Artist(mbid, name);
        artist.setIdentifier(identifier);
        return artist;
    }

}
