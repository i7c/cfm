package org.rliz.cfm.recorder

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@ComponentScan("org.rliz.cfm")
class RecorderApplication

fun main(args: Array<String>) {
    SpringApplication.run(RecorderApplication::class.java, *args)
}
