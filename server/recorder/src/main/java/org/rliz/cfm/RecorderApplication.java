package org.rliz.cfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main class to start the application.
 */
@EnableAutoConfiguration
@ComponentScan("org.rliz.cfm")
public class RecorderApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RecorderApplication.class, args);
    }
}
