package org.rliz.mbs.recording.model;

import org.rliz.mbs.artist.model.ArtistCredit;
import org.rliz.mbs.common.model.FirstClassEntity;
import org.rliz.mbs.release.model.Medium;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents MB track.
 */
@Entity
@Table(name = "track", indexes = {
        @Index(name = "ix_track_id", columnList = "id"),
        @Index(name = "ix_track_recording", columnList = "recording"),
        @Index(name = "ix_track_medium", columnList = "medium")
})
public class Track extends FirstClassEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recording")
    private Recording recording;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medium")
    private Medium medium;

    @Column(name = "position")
    private Integer position;

    @Column(name = "number")
    private String number;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "artist_credit")
    private ArtistCredit artistCredit;

    @Column(name = "length")
    private Integer length;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "is_data_track")
    private Boolean isDataTrack;

//    edits_pending | integer


    public Recording getRecording() {
        return recording;
    }

    public Medium getMedium() {
        return medium;
    }

    public Integer getPosition() {
        return position;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public ArtistCredit getArtistCredit() {
        return artistCredit;
    }

    public Integer getLength() {
        return length;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Boolean getDataTrack() {
        return isDataTrack;
    }
}
