package org.rliz.cfm.playback.model;

import org.rliz.cfm.common.model.AbstractEntity;
import org.rliz.cfm.recording.model.Recording;
import org.rliz.cfm.release.model.ReleaseGroup;
import org.rliz.cfm.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Represents the playback of a single recording. This entity also stores the original information sent by the user
 * such that no data is lost.
 */
@Entity
public class Playback extends AbstractEntity {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_playback_recording"))
    private Recording recording;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_playback_releasegroup"))
    private ReleaseGroup releaseGroup;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_playback_user"))
    @NotNull
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date time;

    @Embedded
    private PlaybackOriginalData originalData;

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
    public Playback(Recording recording, ReleaseGroup releaseGroup, User user, Date time) {
        this.recording = recording;
        this.releaseGroup = releaseGroup;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public PlaybackOriginalData getOriginalData() {
        return originalData;
    }

    public void setOriginalData(PlaybackOriginalData originalData) {
        this.originalData = originalData;
    }

    @Override
    public String getDisplayName() {
        return "Playback " + String.valueOf(getIdentifier());
    }
}
