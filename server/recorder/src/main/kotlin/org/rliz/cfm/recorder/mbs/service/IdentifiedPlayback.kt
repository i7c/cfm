package org.rliz.cfm.recorder.mbs.service

import java.util.UUID

data class IdentifiedPlayback(
    val artistName: String,
    val recordingName: String,
    val releaseGroupName: String,
    val artistId: UUID,
    val releaseGroupId: UUID,
    val recordingId: UUID,
    val length: Long,
    val artistSim: Double,
    val releaseGroupSim: Double,
    val recordingSim: Double,
    val lengthDiff: Long
)
