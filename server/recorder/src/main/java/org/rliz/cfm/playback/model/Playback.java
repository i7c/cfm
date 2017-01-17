package org.rliz.cfm.playback.model;

import org.rliz.cfm.common.model.AbstractEntity;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.release.model.ReleaseGroup;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents the playback of a single recording. This entity also stores the original information sent by the user
 * such that no data is lost.
 */
@Entity
public class Playback extends AbstractEntity {

    @OneToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_playback_recording"))
    private Recording recording;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_playback_releasegroup"))
    private ReleaseGroup releaseGroup;

    private Date time;

    /**
     * Default constructor.
     */
    public Playback() {
        // For JPA
    }

    /**
     * Constructor.
     *
     * @param recording    the played recording, must be non-null
     * @param releaseGroup the associated release group (album), can be null
     * @param time         the time of playback
     */
    public Playback(Recording recording, ReleaseGroup releaseGroup, Date time) {
        this.recording = recording;
        this.releaseGroup = releaseGroup;
        this.time = time;
    }

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }

    public ReleaseGroup getReleaseGroup() {
        return releaseGroup;
    }

    public void setReleaseGroup(ReleaseGroup releaseGroup) {
        this.releaseGroup = releaseGroup;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
