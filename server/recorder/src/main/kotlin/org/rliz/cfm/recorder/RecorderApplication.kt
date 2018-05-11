package org.rliz.cfm.recorder

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAutoConfiguration
@ComponentScan("org.rliz.cfm")
@EnableScheduling
class RecorderApplication

fun main(args: Array<String>) {
    SpringApplication.run(RecorderApplication::class.java, *args)
}
