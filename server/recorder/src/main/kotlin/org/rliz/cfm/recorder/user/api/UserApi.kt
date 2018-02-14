package org.rliz.cfm.recorder.user.api

import org.rliz.cfm.recorder.common.api.toHttpResponse
import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(path = arrayOf("/rec/v1/users"))
class UserApi {

    @Autowired
    lateinit var userBoundary: UserBoundary

    @Transactional
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun createUser(@Valid @RequestBody body: UserRes): ResponseEntity<UserRes> =
            userBoundary.createUser(
                    name = body.name!!,
                    password = body.password!!,
                    state = body.state!!
            )
                    .toRes()
                    .toHttpResponse(HttpStatus.CREATED)

    @Transactional(readOnly = true)
    @RequestMapping(path = arrayOf("/current", "/{uuid}"))
    fun getUser(@PathVariable(name = "uuid", required = false) uuid: UUID?): ResponseEntity<UserRes> =
            (uuid?.let { userBoundary.getUser(uuid) }
                    ?: userBoundary.getCurrentUser())
                    .toRes()
                    .toHttpResponse(HttpStatus.OK)


}

