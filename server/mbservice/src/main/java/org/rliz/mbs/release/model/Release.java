package org.rliz.mbs.release.model;

import org.rliz.mbs.artist.model.ArtistCredit;
import org.rliz.mbs.common.model.FirstClassEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a single release.
 */
@Entity
@Table(name = "release", indexes = {
        @Index(name = "release_pkey", columnList = "id"),
        @Index(name = "release_idx_gid", columnList = "gid"),
        @Index(name = "release_idx_artist_credit", columnList = "artist_credit"),
        @Index(name = "release_idx_name", columnList = "name"),
        @Index(name = "release_idx_release_group", columnList = "release_group")
})
public class Release extends FirstClassEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_credit")
    private ArtistCredit artistCredit;

    @Column(name = "comment")
    private String comment;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_group")
    private ReleaseGroup releaseGroup;

//    status        | integer
//    packaging     | integer
//    language      | integer
//    script        | integer
//    barcode       | character varying(255)
//    edits_pending | integer
//    quality       | smallint

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

    public ReleaseGroup getReleaseGroup() {
        return releaseGroup;
    }

    @Override
    public String getDisplayName() {
        return getName();
    }
}
