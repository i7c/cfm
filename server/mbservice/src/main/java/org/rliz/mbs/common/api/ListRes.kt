package org.rliz.mbs.common.api

class ListRes<out R> constructor(val elements: List<R>) {
    val size: Int = elements.size
}

fun <E, R> List<E>.toRes(translate: (E) -> R): ListRes<R> = ListRes(this.map(translate))
