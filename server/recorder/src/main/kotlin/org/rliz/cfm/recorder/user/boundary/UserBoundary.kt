package org.rliz.cfm.recorder.user.boundary

import org.rliz.cfm.recorder.common.exception.NotFoundException
import org.rliz.cfm.recorder.common.security.AdminAuth
import org.rliz.cfm.recorder.common.security.currentUser
import org.rliz.cfm.recorder.user.data.User
import org.rliz.cfm.recorder.user.data.UserRepo
import org.rliz.cfm.recorder.user.data.UserState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.IdGenerator
import java.util.*


@Service
class UserBoundary {

    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var idgen: IdGenerator

    private val encoder = BCryptPasswordEncoder()

    @AdminAuth
    fun createUser(name: String, password: String, state: UserState, systemUser: Boolean = false): User {
        val user = User(idgen.generateId(), name, encoder.encode(password), state, systemUser)
        return userRepo.save(user)
    }

    fun findUserByName(name: String): User? = userRepo.findOneByName(name)

    @AdminAuth
    fun promoteToSystemUser(user: User): User {
        user.systemUser = true
        return userRepo.save(user)
    }

    private fun findUser(uuid: UUID): User? = userRepo.findOneByUuid(uuid)

    fun getUser(uuid: UUID): User = findUser(uuid) ?: throw NotFoundException(User::class, "uuid $uuid")

    fun getCurrentUser(): User = findUser(currentUser().uuid!!) ?: throw NotFoundException(User::class, "self")

    /**
     * Same as createUser() but does not check any authorization.
     * Use with care and only if you are certain that the calling piece of code is authorized.
     */
    fun createUserNoAuthCheck(name: String, password: String, state: UserState, systemUser: Boolean = false): User =
            createUser(name, password, state, systemUser)

    /**
     * Same as promoteToSystemUser() but does not check any authorization.
     * Use with care and only if you are certain that the calling piece of code is authorized.
     */
    fun promoteToSystemUserNoAuthCheck(user: User): User = this.promoteToSystemUser(user)
}