package org.rliz.cfm.recorder.common.data

import java.util.*
import javax.persistence.*

@MappedSuperclass
open class AbstractModel {
    @Id
    @GeneratedValue
    var oid: Long? = null

    @Column(nullable = false)
    var uuid: UUID? = null

    constructor()

    constructor(uuid: UUID) {
        this.uuid = uuid
    }
}
