package org.rliz.cfm.recorder.common.exception

import org.rliz.cfm.recorder.common.log.logger
import org.rliz.cfm.recorder.common.security.currentUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    companion object {
        val log = logger<ExceptionHandler>()
    }

    fun <E: Exception> logged(e: E): E {
        log.info("${e.message} [${currentUser().uuid}]")
        log.info("Uncaught exception", e)
        return e
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFound(e: NotFoundException): ResponseEntity<String> = logged(e).toRes(HttpStatus.NOT_FOUND)

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDenied(e: AccessDeniedException): ResponseEntity<String> = logged(e).toRes(HttpStatus.FORBIDDEN)

    @ExceptionHandler(OutdatedException::class)
    fun outdated(e: OutdatedException): ResponseEntity<String> = logged(e).toRes(HttpStatus.PRECONDITION_FAILED)

    @ExceptionHandler
    fun defaultHandler(e: Exception): ResponseEntity<String> = logged(e).toRes(HttpStatus.BAD_REQUEST)
}
