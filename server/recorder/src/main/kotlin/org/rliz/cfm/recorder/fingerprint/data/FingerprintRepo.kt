package org.rliz.cfm.recorder.fingerprint.data

import org.rliz.cfm.recorder.user.data.User
import org.springframework.data.jpa.repository.JpaRepository

interface FingerprintRepo : JpaRepository<Fingerprint, Long> {

    fun findOneByUserAndFingerprint(user: User, fingerprint: String): Fingerprint?
}
