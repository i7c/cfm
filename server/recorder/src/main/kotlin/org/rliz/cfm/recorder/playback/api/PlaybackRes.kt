package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.playback.boundary.PlaybackDto
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
    val source: String? = null
)

fun PlaybackDto.toRes(): PlaybackRes = PlaybackRes(
    artists = this.artists,
    recordingTitle = this.recordingTitle,
    releaseTitle = this.releaseTitle,
    timestamp = this.timestamp,
    playTime = this.playTime,
    broken = this.broken,
    id = this.id
)
