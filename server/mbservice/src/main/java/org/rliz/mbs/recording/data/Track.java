package org.rliz.mbs.recording.data;

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
        @Index(name = "track_pkey", columnList = "id"),
        @Index(name = "track_idx_gid", columnList = "gid"),
        @Index(name = "track_idx_artist_credit", columnList = "artist_credit"),
        @Index(name = "track_idx_medium_position", columnList = "medium, position"),
        @Index(name = "track_idx_name", columnList = "name"),
        @Index(name = "track_idx_recording", columnList = "recording"),

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
    @JoinColumn(name = "artist_credit")
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

    @Override
    public String getDisplayName() {
        return getName();
    }
}
