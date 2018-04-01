package org.rliz.mbs

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan

/**
 * Main class to start the application.
 */
@EnableAutoConfiguration
@ComponentScan("org.rliz.mbs")
class MbServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(MbServiceApplication::class.java, *args)
}
