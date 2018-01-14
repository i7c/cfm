package org.rliz.cfm.recorder.playback.data

data class AccumulatedPlaybacks(
        val occurrences: Long?,
        val artistsJson: String?,
        val recordingTitle: String?,
        val releaseTitle: String?,
        val fixAttempt: Long?
)
