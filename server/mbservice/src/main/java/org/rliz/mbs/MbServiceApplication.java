package org.rliz.mbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main class to start the application.
 */
@EnableAutoConfiguration
@ComponentScan("org.rliz.mbs")
public class MbServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MbServiceApplication.class, args);
    }
}
