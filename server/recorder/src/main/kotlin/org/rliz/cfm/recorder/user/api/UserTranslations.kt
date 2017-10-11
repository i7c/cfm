package org.rliz.cfm.recorder.user.api

import org.rliz.cfm.recorder.user.data.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun User.toRes(status: HttpStatus): ResponseEntity<UserRes> =
        ResponseEntity(UserRes(
                name = this.name,
                password = null,
                state = this.state,
                uuid = this.uuid
        ), status)