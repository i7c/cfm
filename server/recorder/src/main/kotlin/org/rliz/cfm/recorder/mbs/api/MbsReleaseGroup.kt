package org.rliz.cfm.recorder.mbs.api

import java.util.*

data class MbsReleaseGroupRes(
        val identifier: UUID? = null,
        val lastUpdated: Long? = null,
        val comment: String? = null,
        val name: String? = null,
        val artists: List<MbsArtistRes>? = null
)

data class MbsReleaseGroupDto(
        val identifier: UUID,
        val lastUpdated: Long?,
        val comment: String? = null,
        val name: String,
        val artists: List<MbsArtistDto>
)
