package org.rliz.cfm.recorder.mbs.api

import org.rliz.cfm.recorder.common.trans.nonEmptyCollection
import org.rliz.cfm.recorder.common.trans.nonNullField
import java.util.*

data class MbsIdentifiedPlaybackRes(
        val releaseId: UUID? = null,
        val releaseGroupId: UUID? = null,
        val recordingId: UUID? = null,
        val recordingArtistIds: List<UUID>? = null,
        val releaseGroupArtistIds: List<UUID> = emptyList()) {

    fun toDto() = MbsIdentifiedPlaybackDto(
            releaseId = nonNullField(this::releaseId),
            releaseGroupId = nonNullField(this::releaseGroupId),
            recordingId = nonNullField(this::recordingId),
            recordingArtistIds = nonEmptyCollection(this::recordingArtistIds),
            releaseGroupArtistIds = nonEmptyCollection(this::releaseGroupArtistIds)
    )
}

data class MbsIdentifiedPlaybackDto(
        val releaseId: UUID,
        val releaseGroupId: UUID,
        val recordingId: UUID,
        val recordingArtistIds: List<UUID>,
        val releaseGroupArtistIds: List<UUID>
)
