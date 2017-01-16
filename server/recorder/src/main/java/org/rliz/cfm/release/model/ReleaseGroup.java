package org.rliz.cfm.release.model;

import org.rliz.cfm.artist.model.Artist;
import org.rliz.cfm.common.model.AbstractEntity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a group of releases, i.e. a release family.
 */
@Entity
public class ReleaseGroup extends AbstractEntity {

    private UUID mbid;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(foreignKey = @ForeignKey(name = "FK_releasegroup_artist"),
        inverseForeignKey = @ForeignKey(name = "FK_artist_releasegroup"))
    private Set<Artist> artists;

    @Column(length = 511, nullable = false)
    private String title;

    /**
     * Default constructor.
     */
    public ReleaseGroup() {
        // for JPA
    }

    /**
     * Field setting constructor.
     *
     * @param mbid    the musicbrainz ID of this release group
     * @param artists the artists of this release group
     * @param title   the title of the release group
     */
    public ReleaseGroup(UUID mbid, Set<Artist> artists, String title) {
        this.mbid = mbid;
        this.artists = artists;
        this.title = title;
    }

    public UUID getMbid() {
        return mbid;
    }

    public void setMbid(UUID mbid) {
        this.mbid = mbid;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
