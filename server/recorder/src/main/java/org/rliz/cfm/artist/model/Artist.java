package org.rliz.cfm.artist.model;

import org.rliz.cfm.common.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Represents an artist.
 */
@Entity
public class Artist extends AbstractEntity {

    private UUID mbid;

    @Column
    @NotNull
    private String name;

    /**
     * Default constructor.
     */
    public Artist() {
        // for JPA
    }

    /**
     * Field setting constructor.
     *
     * @param mbid the musicbrainz ID of this artist, can be null
     * @param name the name of this artist
     */
    public Artist(UUID mbid, String name) {
        this.mbid = mbid;
        this.name = name;
    }

    public UUID getMbid() {
        return mbid;
    }

    public void setMbid(UUID mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
