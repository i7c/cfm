package org.rliz.mbs.release.data

import java.util.UUID

data class ReleaseGroupWithArtist(
    val id: UUID,
    val name: String,
    val joinPhrase: String,
    val artist: String
)

data class ReleaseGroupWithArtists(
    val id: UUID,
    val name: String,
    val artists: List<String>,
    val artistText: String
)
