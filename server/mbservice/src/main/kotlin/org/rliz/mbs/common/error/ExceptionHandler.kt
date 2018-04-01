package org.rliz.mbs.common.error

import org.rliz.mbs.common.api.toHttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun notFound(e: NotFoundException): ResponseEntity<String> =
        (e.message ?: "404 - not found").toHttpResponse(HttpStatus.NOT_FOUND)
}
