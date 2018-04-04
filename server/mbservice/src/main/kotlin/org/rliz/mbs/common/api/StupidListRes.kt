package org.rliz.mbs.common.api

data class StupidListRes<out T>(
    val elements: List<T>,
    val size: Int = elements.size
)
