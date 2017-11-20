package org.rliz.cfm.recorder.common.security

import org.rliz.cfm.recorder.user.data.User
import org.springframework.security.core.context.SecurityContextHolder

fun currentUser(): User = SecurityContextHolder.getContext().authentication.principal as User
