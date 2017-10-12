package org.rliz.cfm.recorder.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun notFound(e: NotFoundException): ResponseEntity<String> = e.toRes(HttpStatus.NOT_FOUND)

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDenied(e: AccessDeniedException): ResponseEntity<String> = e.toRes(HttpStatus.FORBIDDEN)

    @ExceptionHandler
    fun defaultHandler(e: Exception): ResponseEntity<String> = e.toRes(HttpStatus.BAD_REQUEST)
}