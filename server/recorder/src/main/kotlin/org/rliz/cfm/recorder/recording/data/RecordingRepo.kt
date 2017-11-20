package org.rliz.cfm.recorder.recording.data

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RecordingRepo : JpaRepository<Recording, Long> {
    fun findOneByUuid(uuid: UUID): Recording?
}
