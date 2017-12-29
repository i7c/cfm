package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.playback.boundary.AccumulatedPlaybacksDto

class AccumulatedPlaybackRes(
        val occurrences: Long,
        val artistsJson: String,
        val artists: List<String>,
        val recordingTitle: String,
        val releaseTitle: String
)

fun AccumulatedPlaybacksDto.toRes() = AccumulatedPlaybackRes(
        occurrences = this.occurrences,
        artistsJson = this.artistsJson,
        artists = this.artists,
        recordingTitle = this.recordingTitle,
        releaseTitle = this.releaseTitle
)
