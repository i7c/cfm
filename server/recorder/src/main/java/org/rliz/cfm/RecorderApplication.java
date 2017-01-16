package org.rliz.cfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main class to start the application.
 */
@EnableAutoConfiguration
@ComponentScan("org.rliz.cfm")
@EnableFeignClients
public class RecorderApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RecorderApplication.class, args);
    }
}
