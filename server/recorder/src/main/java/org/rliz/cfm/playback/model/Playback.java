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
}
