package org.rliz.cfm.recorder.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Exception.toRes(status: HttpStatus): ResponseEntity<String> =
        if (this.message == null)
            ResponseEntity("Fatal ${this::class.qualifiedName} without message", status)
        else
            ResponseEntity(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now()) + ": ${this.message}", status)
