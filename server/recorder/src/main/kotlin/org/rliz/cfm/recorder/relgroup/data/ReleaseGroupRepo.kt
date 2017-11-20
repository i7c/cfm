package org.rliz.cfm.recorder.relgroup.data

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReleaseGroupRepo : JpaRepository<ReleaseGroup, Long> {

    fun findOneByUuid(uuid: UUID): ReleaseGroup?

}
