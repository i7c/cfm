package org.rliz.cfm.recorder.common.api

import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ListRes<E, R> constructor(private val l: Page<E>, private val translate: (E) -> R) {

    val elements: List<R> = l.content.map(translate).toList()

    fun getSize(): Int = l.size
    fun getCount(): Int = l.numberOfElements
    fun getNumber(): Int = l.number

    fun getTotal(): Long = l.totalElements
    fun getTotalPages(): Int = l.totalPages

    fun toHttpResponse(status: HttpStatus): ResponseEntity<ListRes<E, R>> = ResponseEntity(this, status)
}

fun <E, R> Page<E>.toRes(translate: (E) -> R): ListRes<E, R> = ListRes(this, translate)
