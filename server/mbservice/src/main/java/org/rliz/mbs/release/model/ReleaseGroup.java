package org.rliz.mbs.release.model;

import org.rliz.mbs.artist.model.ArtistCredit;
import org.rliz.mbs.common.model.FirstClassEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Represents a group of {@link Release}s.
 */
@Entity
public class ReleaseGroup extends FirstClassEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "artist_credit")
    private ArtistCredit artistCredit;

    @Column(name = "comment")
    private String comment;

    @Column(name = "last_updated")
    private Date lastUpdated;

//    type          | integer
//    edits_pending | integer

    public String getName() {
        return name;
    }

    public ArtistCredit getArtistCredit() {
        return artistCredit;
    }

    public String getComment() {
        return comment;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
