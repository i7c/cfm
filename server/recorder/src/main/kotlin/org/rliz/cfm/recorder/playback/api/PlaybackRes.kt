package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.playback.data.Playback
import java.util.UUID
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class PlaybackRes(
    @field:NotNull @field:Size(min = 1) val artists: List<String> = emptyList(),
    @field:NotNull @field:Size(min = 1) val recordingTitle: String? = null,
    @field:NotNull @field:Size(min = 1) val releaseTitle: String? = null,
    val timestamp: Long? = null,
    val playTime: Long? = null,
    val trackLength: Long? = null,
    val discNumber: Int? = null,
    val trackNumber: Int? = null,
    val broken: Boolean? = null,
    val id: UUID? = null,
    val source: String? = null,
    val fixAttempt: Long? = null
)

fun Playback.toRes(): PlaybackRes = PlaybackRes(
    artists = this.artists,
    recordingTitle = this.recording,
    releaseTitle = this.release,
    timestamp = this.timestamp,
    playTime = this.playTime,
    broken = this.releaseGroupUuid == null || this.recordingUuid == null,
    id = this.id,
    fixAttempt = this.fixAttempt
)
