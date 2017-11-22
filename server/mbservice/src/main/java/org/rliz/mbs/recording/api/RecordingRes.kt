package org.rliz.mbs.recording.api

data class RecordingRes(
        val name: String? = null,
        val length: Long? = null,
        val artists: List<String> = emptyList()
)
