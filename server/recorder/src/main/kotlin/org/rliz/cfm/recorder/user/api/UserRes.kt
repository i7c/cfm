package org.rliz.cfm.recorder.user.api

import org.rliz.cfm.recorder.user.data.User
import org.rliz.cfm.recorder.user.data.UserState
import java.util.UUID
import javax.validation.constraints.Size

data class UserRes(
    @field:Size(min = 3, max = 128) val name: String? = null,
    @field:Size(min = 8, max = 128) val password: String? = null,
    val state: UserState? = UserState.INACTIVE,
    val uuid: UUID? = null,
    val systemUser: Boolean = false
)

fun User.toRes(): UserRes =
    UserRes(
        name = this.name,
        password = null, // probably never ever touch this
        state = this.state,
        uuid = this.uuid,
        systemUser = this.systemUser
    )
