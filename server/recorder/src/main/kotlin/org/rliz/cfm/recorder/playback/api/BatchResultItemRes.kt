package org.rliz.cfm.recorder.playback.api

import org.rliz.cfm.recorder.playback.boundary.BatchResultItem

data class BatchResultItemRes(
        val success: Boolean = false
)

fun BatchResultItem.toRes() = BatchResultItemRes(success = this.success)
