package org.rliz.cfm.recorder.common.api

import org.springframework.data.domain.Page

class PageRes<out R>(l: Page<R>) {
    val elements: List<R> = l.content
    val size: Int = l.size
    val count: Int = l.numberOfElements
    val number: Int = l.number
    val total: Long = l.totalElements
    val totalPages: Int = l.totalPages
}

fun <E, R> Page<E>.toRes(translate: (E) -> R): PageRes<R> = PageRes(this.map(translate))
