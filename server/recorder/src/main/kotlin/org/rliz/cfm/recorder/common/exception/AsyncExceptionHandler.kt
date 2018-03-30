package org.rliz.cfm.recorder.common.exception

import org.rliz.cfm.recorder.common.log.logger
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    companion object {
        val log = logger<AsyncExceptionHandler>()
    }

    override fun handleUncaughtException(e: Throwable, m: Method, vararg args: Any?) {
        log.error("Uncaught async exception: {}", e)
    }
}
