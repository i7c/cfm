package org.rliz.cfm.recorder.user.data

import org.rliz.cfm.recorder.common.data.AbstractModel
import javax.persistence.*

@Entity
@Table(name = "cfm_user",
        uniqueConstraints = arrayOf(UniqueConstraint(name = "UC_User_name", columnNames = arrayOf("name")))
)
data class User constructor(@field:Column(length = 128, nullable = false) val name: String,
                            @field:Column(length = 128, nullable = false) val password: String,
                            @Enumerated(value = EnumType.STRING) val state: UserState)
    : AbstractModel()
