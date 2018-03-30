package org.rliz.cfm.recorder.playback.boundary

import org.rliz.cfm.recorder.playback.data.Playback
import java.util.UUID

data class PlaybackDto(
    val artists: List<String> = emptyList(),
    val recordingTitle: String? = null,
    val releaseTitle: String? = null,
    val timestamp: Long? = null,
    val playTime: Long? = null,
    val trackLength: Long? = null,
    val discNumber: Int? = null,
    val trackNumber: Int? = null,
    val broken: Boolean? = null,
    val id: UUID? = null
)

fun Playback.toDto(): PlaybackDto =
    PlaybackDto(
        artists = this.originalData?.artists ?: emptyList(),
        recordingTitle = this.originalData?.recordingTitle,
        releaseTitle = this.originalData?.releaseTitle,
        timestamp = this.timestamp,
        playTime = this.playTime,
        trackLength = this.originalData?.length,
        discNumber = this.originalData?.discNumber,
        trackNumber = this.originalData?.trackNumber,
        broken = (this.recordingUuid == null || this.releaseGroupUuid == null),
        id = this.uuid
    )
