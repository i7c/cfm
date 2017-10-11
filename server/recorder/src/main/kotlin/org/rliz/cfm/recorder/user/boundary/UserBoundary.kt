package org.rliz.cfm.recorder.user.boundary

import org.rliz.cfm.recorder.user.data.User
import org.rliz.cfm.recorder.user.data.UserRepo
import org.rliz.cfm.recorder.user.data.UserState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.IdGenerator
import java.util.*


@Service
class UserBoundary {

    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var idgen: IdGenerator

    fun createUser(name: String, password: String, state: UserState): User {
        val user = User(name, password, state)
        user.uuid = idgen.generateId()
        return userRepo.save(user)
    }

}