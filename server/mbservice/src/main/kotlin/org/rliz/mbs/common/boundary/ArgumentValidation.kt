package org.rliz.mbs.common.boundary

fun <T> limited(l: List<T>, maxSize: Int = 50) =
    if (l.size > maxSize) throw IllegalArgumentException("Argument exceeds max size of $maxSize")
    else l
