package org.rliz.cfm.recorder.mbs.api

import java.util.*

data class MbsReleaseGroupRes(
        val identifier: UUID? = null,
        val lastUpdated: Date? = null,
        val comment: String? = null,
        val name: String? = null,
        val artists: List<MbsArtistRes>? = null
)

data class MbsReleaseGroupDto(
        val identifier: UUID,
        val lastUpdated: Date,
        val comment: String? = null,
        val name: String,
        val artists: List<MbsArtistDto>
)
