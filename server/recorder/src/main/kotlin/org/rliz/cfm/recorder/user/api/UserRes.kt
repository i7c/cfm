package org.rliz.cfm.recorder.user.api

import org.rliz.cfm.recorder.user.data.UserState
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserRes(
        @field:NotNull @field:Size(min = 3, max = 128) val name: String? = null,
        @field:NotNull @field:Size(min = 8, max = 128) val password: String? = null,
        val state: UserState = UserState.INACTIVE,
        val uuid: UUID? = null
)

