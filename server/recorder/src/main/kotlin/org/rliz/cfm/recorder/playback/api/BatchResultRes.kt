package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.playback.boundary.BatchResultItem

data class BatchResultRes(
        val results: List<BatchResultItemRes>
)

fun List<BatchResultItem>.toRes() = BatchResultRes(results = map(BatchResultItem::toRes))
