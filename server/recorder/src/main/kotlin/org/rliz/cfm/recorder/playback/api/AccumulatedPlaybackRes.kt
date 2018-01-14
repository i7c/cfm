package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.playback.boundary.AccumulatedPlaybacksDto
import java.util.*
import javax.validation.constraints.NotNull

class AccumulatedPlaybackRes(
        val occurrences: Long = 0,
        val artistsJson: String = "",
        val artists: List<String> = emptyList(),
        val recordingTitle: String = "",
        val releaseTitle: String = "",
        @field:NotNull val recordingId: UUID? = null,
        @field:NotNull val releaseGroupId: UUID? = null
)

fun AccumulatedPlaybacksDto.toRes() = AccumulatedPlaybackRes(
        occurrences = this.occurrences,
        artistsJson = this.artistsJson,
        artists = this.artists,
        recordingTitle = this.recordingTitle,
        releaseTitle = this.releaseTitle
)
