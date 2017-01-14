package cfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Main class to start the application.
 */
@EnableAutoConfiguration
public class RecorderApplication {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(RecorderApplication.class, args);
    }
}
