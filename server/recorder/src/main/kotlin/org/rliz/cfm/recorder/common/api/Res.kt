package org.rliz.cfm.recorder.common.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> T.toHttpResponse(status: HttpStatus) = ResponseEntity<T>(this, status)
