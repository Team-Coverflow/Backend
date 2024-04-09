package com.coverflow;

import com.coverflow.global.config.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableConfigurationProperties(CorsProperties.class)
public class CoverflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoverflowApplication.class, args);
    }

}
