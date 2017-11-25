package org.rliz.cfm.recorder.playback.boundary

import org.rliz.cfm.recorder.playback.api.PlaybackRes
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class PlaybackDto(
        @field:NotNull @field:Size(min = 1) val artists: List<String> = emptyList(),
        @field:NotNull @field:Size(min = 1) val recordingTitle: String = "",
        @field:NotNull @field:Size(min = 1) val releaseTitle: String = "",
        val timestamp: Long? = null,
        val playTime: Long? = null,
        val trackLength: Long? = null,
        val discNumber: Int? = null,
        val trackNumber: Int? = null,
        val broken: Boolean? = null,
        val id: UUID? = null) {

    fun toRes(): PlaybackRes = PlaybackRes(
            artists = this.artists,
            recordingTitle = this.recordingTitle,
            releaseTitle = this.releaseTitle,
            timestamp = this.timestamp,
            playTime = this.playTime,
            broken = this.broken,
            id = this.id
    )
}
