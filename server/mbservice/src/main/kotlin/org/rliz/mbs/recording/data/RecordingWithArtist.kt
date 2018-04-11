package org.rliz.mbs.recording.data

import java.util.UUID

data class RecordingWithArtist(
    val id: UUID,
    val name: String,
    val length: Long,
    val artist: String,
    val joinPhrase: String
)

data class RecordingWithArtists(
    val id: UUID,
    val name: String,
    val length: Long,
    val artists: List<String>,
    val artistText: String
)
