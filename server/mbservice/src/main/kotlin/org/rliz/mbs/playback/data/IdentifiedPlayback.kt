package org.rliz.mbs.playback.data

import java.util.UUID

data class IdentifiedPlayback(
    val artistName: String,
    val releaseGroupName: String,
    val recordingName: String,
    val artistId: UUID,
    val releaseGroupId: UUID,
    val recordingId: UUID,
    val length: Long,
    val artistSim: Double,
    val releaseGroupSim: Double,
    val recordingSim: Double,
    val lengthDiff: Long
)
