package org.rliz.cfm.recorder.mbs.api

import java.util.*

data class MbsArtistRes(
        val comment: String? = null,
        val name: String? = null,
        val typeName: String? = null,
        val ended: Boolean? = null,
        val identifier: UUID? = null,
        val sortName: String? = null,
        val beginDateYear: Int? = null,
        val lastUpdated: Long? = null,
        val areaName: String? = null
)

data class MbsArtistDto(
        val comment: String? = null,
        val name: String,
        val typeName: String? = null,
        val ended: Boolean? = null,
        val identifier: UUID,
        val sortName: String? = null,
        val beginDateYear: Int? = null,
        val lastUpdated: Long?,
        val areaName: String? = null
)
