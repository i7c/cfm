package org.rliz.cfm.recorder.user.data

import org.rliz.cfm.recorder.common.data.AbstractModel
import javax.persistence.*

@Entity
@Table(name = "cfm_user",
        uniqueConstraints = arrayOf(UniqueConstraint(name = "UC_User_name", columnNames = arrayOf("name")))
)
class User : AbstractModel {

    @Column(length = 128, nullable = false)
    var name: String? = null

    @Column(length = 128, nullable = false)
    var password: String? = null

    @Enumerated(value = EnumType.STRING)
    var state: UserState? = null


    constructor() : super()

    constructor(name: String, password: String, state: UserState) : this() {
        this.name = name
        this.password = password
        this.state = state
    }

}
