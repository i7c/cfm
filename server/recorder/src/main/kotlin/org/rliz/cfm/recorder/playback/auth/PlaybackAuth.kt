package org.rliz.cfm.recorder.playback.auth

import org.rliz.cfm.recorder.common.exception.NotAuthorizedException
import org.rliz.cfm.recorder.common.security.currentUser
import org.rliz.cfm.recorder.playback.data.Playback


fun demandOwnership(p: Playback): Unit =
        if (currentUser().uuid == null || currentUser().uuid != p.user!!.uuid)
            throw NotAuthorizedException(p::class)
        else Unit