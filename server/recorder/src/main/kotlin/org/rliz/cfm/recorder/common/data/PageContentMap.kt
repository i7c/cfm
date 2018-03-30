package org.rliz.cfm.recorder.common.data

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

fun <T, R> Page<T>.contentMap(transform: (List<T>) -> (List<R>)) = PageImpl(
    transform(this.content),
    PageRequest.of(
        this.number,
        this.size,
        this.sort
    ),
    this.totalElements
)
