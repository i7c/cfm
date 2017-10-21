package org.rliz.cfm.recorder.playback.api

import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class PlaybackRes(
        @field:NotNull @field:Size(min = 1) val artists: List<String> = emptyList(),
        @field:NotNull @field:Size(min = 1) val recordingTitle: String = "",
        @field:NotNull @field:Size(min = 1) val releaseTitle: String = "",
        val timestamp: Long? = null,
        val playTime: Long? = null,
        val trackLength: Long? = null,
        val discNumber: Int? = null,
        val trackNumber: Int? = null,
        val broken: Boolean? = null,
        val id: UUID? = null
)