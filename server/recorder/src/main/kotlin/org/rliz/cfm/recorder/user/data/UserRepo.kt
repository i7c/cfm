package org.rliz.cfm.recorder.user.data

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepo : JpaRepository<User, Long> {

    fun findOneByName(name: String): User?

    fun findOneByUuid(uuid: UUID): User?

}