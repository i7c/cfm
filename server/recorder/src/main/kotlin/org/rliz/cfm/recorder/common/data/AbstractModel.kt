package org.rliz.cfm.recorder.common.data

import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class AbstractModel {
    @Id
    @GeneratedValue
    var oid: Long? = null

    @Column(nullable = false)
    var uuid: UUID? = null
}