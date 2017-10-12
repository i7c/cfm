package org.rliz.cfm.recorder.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun Exception.toRes(status: HttpStatus): ResponseEntity<String> =
        ResponseEntity(this.message ?: "Fatal ${this::class.qualifiedName} without message", status)
