package org.rliz.cfm.recorder.user.boundary

import org.rliz.cfm.recorder.user.data.User
import org.rliz.cfm.recorder.user.data.UserRepo
import org.rliz.cfm.recorder.user.data.UserState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserBoundary {

    @Autowired
    lateinit var userRepo: UserRepo

    fun createUser(name: String, password: String, state: UserState): User {
        val user = User(name, password, state)
        user.uuid = UUID.randomUUID()
        return userRepo.save(user)
    }

}