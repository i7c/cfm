package org.rliz.cfm.recorder.playback.boundary

import org.rliz.cfm.recorder.playback.data.AccumulatedPlaybacks

data class AccumulatedPlaybacksDto(
    val occurrences: Long,
    val artistsJson: String,
    val artists: List<String>,
    val recordingTitle: String,
    val releaseTitle: String
)

fun AccumulatedPlaybacks.toDto(artistList: List<String>) = AccumulatedPlaybacksDto(
    occurrences = this.occurrences!!,
    artistsJson = this.artistsJson!!,
    artists = artistList,
    recordingTitle = this.recordingTitle!!,
    releaseTitle = this.releaseTitle!!
)
