package org.rliz.mbs.recording.model;

import org.rliz.mbs.artist.model.ArtistCredit;
import org.rliz.mbs.common.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a musicbrainz Recording.
 */
@Entity
@Table(
        name = "recording",
        indexes = {
                @Index(name = "ix_recording_artistcredit", columnList = "artist_credit")
        }
)
public class Recording extends AbstractEntity {

    @NotNull
    @Column(name = "gid")
    private UUID identifier;

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

//    Not mapped yet:
//    edits_pending | integer


    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public ArtistCredit getArtistCredit() {
        return artistCredit;
    }

    public void setArtistCredit(ArtistCredit artistCredit) {
        this.artistCredit = artistCredit;
    }
}