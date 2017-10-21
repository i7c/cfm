package org.rliz.cfm.recorder.artist.data

import org.rliz.cfm.recorder.common.data.AbstractModel
import java.util.*
import javax.persistence.Entity
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class Artist : AbstractModel {

    @NotNull
    @Size(min = 1)
    var name: String? = null

    var lastUpdated: Long? = null

    constructor() : super()

    constructor(uuid: UUID, name: String, lastUpdated: Long?) : super(uuid) {
        this.name = name
        this.lastUpdated = lastUpdated
    }
}