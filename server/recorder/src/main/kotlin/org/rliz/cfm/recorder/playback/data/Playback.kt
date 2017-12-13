package org.rliz.cfm.recorder.playback.data

import org.rliz.cfm.recorder.common.data.AbstractModel
import org.rliz.cfm.recorder.user.data.User
import java.time.Instant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
        indexes = arrayOf(
                Index(
                        name = "IX_Playback_uuid",
                        columnList = "uuid",
                        unique = true
                ),

                Index(
                        name = "IX_Playback_RecordingUuid",
                        columnList = "recording_uuid",
                        unique = false
                ),

                Index(
                        name = "IX_Playback_ReleaseGroupUuid",
                        columnList = "release_group_uuid",
                        unique = false
                ),

                Index(
                        name = "IX_Playback_User",
                        columnList = "user_oid",
                        unique = false
                ),

                Index(
                        name = "IX_Playback_UserAndSource",
                        columnList = "user_oid, source",
                        unique = false
                ),

                Index(
                        name = "IX_Playback_OriginalData",
                        columnList = "original_data_oid",
                        unique = true
                )
        )
)
open class Playback : AbstractModel {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = ForeignKey(name = "FK_Playback_User"))
    var user: User? = null

    @NotNull
    var timestamp: Long? = null

    var playTime: Long? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true, foreignKey = ForeignKey(name = "FK_Playback_OriginalData"))
    var originalData: RawPlaybackData? = null

    @Column(name = "recording_uuid")
    var recordingUuid: UUID? = null

    @Column(name = "release_group_uuid")
    var releaseGroupUuid: UUID? = null

    var source: String? = null

    constructor() : super()

    constructor(uuid: UUID, user: User?, timestamp: Long? = Instant.now().epochSecond, playTime: Long? = null,
                originalData: RawPlaybackData? = null, source: String?) : super(uuid) {
        this.user = user
        this.timestamp = timestamp
        this.playTime = playTime
        this.originalData = originalData
        this.source = source
    }

}
