package org.rliz.cfm.recorder.playback.api

data class PatchPlaybackRes(
    val artists: List<String>? = null,
    val recordingTitle: String? = null,
    val releaseTitle: String? = null,
    val timestamp: Long? = null,
    val playTime: Long? = null,
    val trackLength: Long? = null,
    val discNumber: Int? = null,
    val trackNumber: Int? = null,
    val broken: Boolean? = null
)
