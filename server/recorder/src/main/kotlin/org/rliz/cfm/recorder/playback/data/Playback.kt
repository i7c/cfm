package org.rliz.cfm.recorder.playback.data

import java.util.UUID

data class Playback(
    val id: UUID,

    val artists: List<String>,
    val release: String,
    val recording: String,

    val playTime: Long? = null,
    val releaseGroupUuid: UUID? = null,
    val recordingUuid: UUID? = null,
    val timestamp: Long,

    val rawArtists: List<String>,
    val rawRelease: String,
    val rawRecording: String
)
