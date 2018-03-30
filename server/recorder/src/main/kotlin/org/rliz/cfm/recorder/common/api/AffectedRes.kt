package org.rliz.cfm.recorder.common.api

data class AffectedRes(
    val affected: Long
)

fun Long.toRes(): AffectedRes = AffectedRes(this)
