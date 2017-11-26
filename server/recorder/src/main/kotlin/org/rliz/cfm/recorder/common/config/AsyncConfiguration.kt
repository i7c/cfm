package org.rliz.cfm.recorder.common.config

import org.rliz.cfm.recorder.common.exception.AsyncExceptionHandler
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfiguration : AsyncConfigurer {

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler = AsyncExceptionHandler()

    override fun getAsyncExecutor(): Executor = ThreadPoolTaskExecutor().apply {
        corePoolSize = 4
        maxPoolSize = 16
        setQueueCapacity(128)
        threadNamePrefix = "recorder-async-"
        initialize()
    }
}
