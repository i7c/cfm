package org.rliz.cfm.recorder.playback.data

import org.rliz.cfm.recorder.common.data.AbstractModel
import org.rliz.cfm.recorder.recording.data.Recording
import org.rliz.cfm.recorder.relgroup.data.ReleaseGroup
import org.rliz.cfm.recorder.user.data.User
import java.time.Instant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
open class Playback : AbstractModel {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_Playback_User"))
    var user: User? = null

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var timestamp: Date? = null

    var playTime: Long? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true, foreignKey = ForeignKey(name = "FK_Playback_OriginalData"))
    var originalData: RawPlaybackData? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_Playback_Recording"))
    var recording: Recording? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_Playback_ReleaseGroup"))
    var releaseGroup: ReleaseGroup? = null

    constructor() : super()

    constructor(uuid: UUID, user: User?, timestamp: Date? = Date.from(Instant.now()), playTime: Long? = null,
                originalData: RawPlaybackData? = null, recording: Recording? = null,
                releaseGroup: ReleaseGroup? = null) : super(uuid) {
        this.user = user
        this.timestamp = timestamp
        this.playTime = playTime
        this.originalData = originalData
        this.recording = recording
        this.releaseGroup = releaseGroup
    }

}
