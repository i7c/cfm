package org.rliz.cfm.recorder

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableAutoConfiguration
class RecorderApplication

fun main(args: Array<String>) {
    SpringApplication.run(RecorderApplication::class.java, *args)
}
