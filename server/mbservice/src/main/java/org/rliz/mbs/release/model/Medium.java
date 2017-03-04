package org.rliz.mbs.release.model;

import org.rliz.mbs.common.model.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a MB medium.
 */
@Entity
@Table(name = "medium", indexes = {
        @Index(name = "medium_pkey", columnList = "id"),
        @Index(name = "medium_idx_release_position", columnList = "release, position"),
        @Index(name = "medium_idx_track_count", columnList = "track_count"),
})
public class Medium extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release")
    private Release release;

    @Column(name = "position")
    private Integer position;

    @Column(name = "name")
    private String name;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "track_count")
    private Integer trackCount;

//    format        | integer
//    edits_pending | integer


    public Release getRelease() {
        return release;
    }

    public Integer getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

}
