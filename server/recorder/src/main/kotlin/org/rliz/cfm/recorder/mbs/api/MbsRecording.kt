package org.rliz.cfm.recorder.mbs.api

import java.util.*

data class MbsRecordingRes(
        val identifier: UUID? = null,
        val lastUpdated: Date? = null,
        val length: Long? = null,
        val name: String? = null,
        val comment: String? = null,
        val artists: List<MbsArtistRes>? = null
)

data class MbsRecordingDto(
        val identifier: UUID,
        val lastUpdated: Date,
        val length: Long,
        val name: String,
        val comment: String? = null,
        val artists: List<MbsArtistDto>
)
