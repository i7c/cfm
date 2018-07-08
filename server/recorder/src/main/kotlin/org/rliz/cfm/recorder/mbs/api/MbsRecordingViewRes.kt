package org.rliz.cfm.recorder.mbs.api

import java.util.UUID

data class MbsRecordingViewRes(
    val id: UUID? = null,
    val name: String? = null,
    val length: Long? = null,
    val artists: List<String> = emptyList()
)

data class MbsRecordingViewListRes(
    val elements: List<MbsRecordingViewRes> = emptyList()
) {

    fun toIdMap() = elements.map { it.id!! to it }.toMap()
}
