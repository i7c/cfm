package org.rliz.cfm.recorder.user.data

import org.rliz.cfm.recorder.common.data.AbstractModel
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Index
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "cfm_user",
    uniqueConstraints = arrayOf(
        UniqueConstraint(
            name = "UC_User_name",
            columnNames = arrayOf("name")
        )
    ),
    indexes = arrayOf(
        Index(
            name = "IX_User_uuid",
            columnList = "uuid",
            unique = true
        )
    )
)
class User : AbstractModel {

    @Column(length = 128, nullable = false)
    var name: String? = null

    @Column(length = 128, nullable = false)
    var password: String? = null

    @Enumerated(value = EnumType.STRING)
    var state: UserState? = null

    var systemUser: Boolean = false

    constructor() : super()

    constructor(
        uuid: UUID,
        name: String,
        password: String,
        state: UserState,
        systemUser: Boolean
    ) : super(uuid) {
        this.name = name
        this.password = password
        this.state = state
        this.systemUser = systemUser
    }
}
