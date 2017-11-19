package org.rliz.mbs.recording.data;

import org.rliz.mbs.artist.model.ArtistCredit;
import org.rliz.mbs.common.model.FirstClassEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Represents a musicbrainz Recording.
 */
@Entity
@Table(name = "recording", indexes = {
        @Index(name = "recording_pkey", columnList = "id"),
        @Index(name = "recording_idx_gid", columnList = "gid"),
        @Index(name = "recording_idx_artist_credit", columnList = "artist_credit"),
        @Index(name = "recording_idx_name", columnList = "name")
})
public class Recording extends FirstClassEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "length")
    private Long length;

    @Column(name = "comment")
    private String comment;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "video")
    private Boolean video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_credit", referencedColumnName = "id")
    private ArtistCredit artistCredit;

    @OneToMany(mappedBy = "recording", fetch = FetchType.LAZY)
    private Set<Track> track;

//    Not mapped yet:
//    edits_pending | integer

    public String getName() {
        return name;
    }

    public Long getLength() {
        return length;
    }

    public String getComment() {
        return comment;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Boolean getVideo() {
        return video;
    }

    public ArtistCredit getArtistCredit() {
        return artistCredit;
    }

    public Set<Track> getTrack() {
        return track;
    }

    @Override
    public String getDisplayName() {
        return getName();
    }
}
