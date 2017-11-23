package org.rliz.cfm.recorder.common.api

import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PageRes<E, R> constructor(l: Page<E>, translate: (E) -> R) {

    val elements: List<R> = l.content.map(translate).toList()

    val size: Int = l.size
    val count: Int = l.numberOfElements
    val number: Int = l.number
    val total: Long = l.totalElements
    val totalPages: Int = l.totalPages

    fun toHttpResponse(status: HttpStatus): ResponseEntity<PageRes<E, R>> = ResponseEntity(this, status)
}

fun <E, R> Page<E>.toRes(translate: (E) -> R): PageRes<E, R> = PageRes(this, translate)
