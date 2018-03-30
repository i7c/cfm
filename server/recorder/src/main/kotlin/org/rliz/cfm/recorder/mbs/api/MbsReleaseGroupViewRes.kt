package org.rliz.cfm.recorder.mbs.api

import java.util.UUID

data class MbsReleaseGroupViewRes(
    val id: UUID? = null,
    val name: String? = null,
    val artists: List<String> = emptyList()
)

data class MbsReleaseGroupViewListRes(
    val elements: List<MbsReleaseGroupViewRes> = emptyList()
)
