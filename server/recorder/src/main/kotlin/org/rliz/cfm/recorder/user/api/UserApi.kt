package org.rliz.cfm.recorder.user.api

import org.rliz.cfm.recorder.common.api.toHttpResponse
import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
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
    @RequestMapping(method = [RequestMethod.GET], path = ["/current", "/{id}"])
    fun getUser(
        @PathVariable(name = "id", required = false) uuid: UUID?
    ): ResponseEntity<UserRes> =
        (uuid?.let { userBoundary.getUser(uuid) }
            ?: userBoundary.getCurrentUser())
            .toRes()
            .toHttpResponse(HttpStatus.OK)

    @Transactional
    @RequestMapping(method = [RequestMethod.PATCH], path = ["/current"])
    fun putUser(@Valid @RequestBody body: UserRes): ResponseEntity<UserRes> =
        userBoundary.updateUser(
            body.name,
            body.password
        )
            .toRes()
            .toHttpResponse(HttpStatus.OK)
}
