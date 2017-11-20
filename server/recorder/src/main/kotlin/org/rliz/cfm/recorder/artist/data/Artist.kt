package org.rliz.cfm.recorder.artist.data

import org.rliz.cfm.recorder.common.data.AbstractModel
import java.util.*
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(
        indexes = arrayOf(
                Index(
                        name = "IX_Artist_Identifier",
                        columnList = "uuid",
                        unique = true
                ))
)
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
