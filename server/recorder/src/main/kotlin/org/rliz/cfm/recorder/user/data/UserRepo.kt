package org.rliz.cfm.recorder.user.data

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo : JpaRepository<User, Long> {
}