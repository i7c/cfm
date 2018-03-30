package org.rliz.cfm.recorder.common.data

import org.springframework.stereotype.Component
import org.springframework.util.IdGenerator
import java.util.UUID

@Component
class CfmIdGenerator : IdGenerator {

    override fun generateId(): UUID = UUID.randomUUID()
}
