package org.rliz.cfm.recording.model;

import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.model.AbstractEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a single recording.
 */
@Entity
public class Recording extends AbstractEntity {

    private UUID mbid;

    @Column(length = 512, nullable = false)
    String title;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_recording_artist"), nullable = false)
    private Set<Artist> artists;

    /**
     * Default constructor.
     */
    public Recording() {
        // for JPA
    }

    /**
     * Field setting constructor.
     *
     * @param mbid    musicbrainz identifier, can be null
     * @param title   title of the recording
     * @param artists the set of artists who are credited for this recording
     */
    public Recording(UUID mbid, String title, Set<Artist> artists) {
        this.mbid = mbid;
        this.title = title;
        this.artists = artists;
    }

    public UUID getMbid() {
        return mbid;
    }

    public void setMbid(UUID mbid) {
        this.mbid = mbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

}
