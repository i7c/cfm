package org.rliz.cfm.recorder.user.api

import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(path = arrayOf("/rec/v1/users"))
class UserApi {

    @Autowired
    lateinit var userBoundary: UserBoundary

    @Transactional
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun createUser(@Valid @RequestBody body: UserRes): ResponseEntity<UserRes> = userBoundary.createUser(
            name = body.name!!,
            password = body.password!!,
            state = body.state!!
    ).toRes(HttpStatus.OK)

}

