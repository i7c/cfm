package org.rliz.cfm.recorder.common.lifecycle

import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.rliz.cfm.recorder.user.data.UserState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.SmartLifecycle
import org.springframework.stereotype.Component

val PHASE_SYSTEM_INIT = 100

@Component
class SystemInit : SmartLifecycle {

    private var running = false

    @Autowired
    lateinit var userBoundary: UserBoundary

    @Value("\${cfm.system-user:cfmadmin}")
    lateinit var systemUserName: String

    @Value("\${cfm.system-password:changeme}")
    lateinit var systemPassword: String

    override fun isRunning(): Boolean = running

    override fun start() {
        if (running) throw RuntimeException("SystemInit is already running")

        systemUserInit()

        running = true
    }

    override fun isAutoStartup(): Boolean = true

    override fun stop(callback: Runnable?) {
        running = false
        callback!!.run()
    }

    override fun stop() {
        running = false
    }

    override fun getPhase(): Int = PHASE_SYSTEM_INIT

    private fun systemUserInit() {
        val foundUser = userBoundary.findUserByName(systemUserName)
        if (foundUser == null) {
            userBoundary.createUser(systemUserName, systemPassword, UserState.ACTIVE)
        }
    }
}
