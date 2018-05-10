package org.rliz.cfm.recorder.playback.data

data class PlaybackGroup(
    val occurrences: Long,
    val artists: List<String>,
    val recordingTitle: String,
    val releaseTitle: String,
    val fixAttempt: Long?,
    val length: Long?
) {
    override fun toString(): String =
        "PlaybackGroup(occurrences=$occurrences, artists=$artists, " +
            "recordingTitle='$recordingTitle', releaseTitle='$releaseTitle', " +
            "fixAttempt=$fixAttempt, length=$length)"
}
