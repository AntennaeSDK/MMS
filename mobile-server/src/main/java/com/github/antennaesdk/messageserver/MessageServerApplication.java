package com.github.antennaesdk.messageserver;

import com.github.antennaesdk.messageserver.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Created by snambi on 6/25/16.
 */
@SpringBootApplication
@EnableAutoConfiguration
@Import(ApplicationConfig.class)
@ComponentScan( basePackages = { "com.github.antennaesdk.common", "com.github.antennaesdk.messageserver"})
public class MessageServerApplication {
    public static void main(String[] args) {
        SpringApplication.run( MessageServerApplication.class, args);
    }
}
