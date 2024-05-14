package com.coverflow;

import com.coverflow.global.config.CorsProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableConfigurationProperties(CorsProperties.class)
public class CoverflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoverflowApplication.class, args);
    }

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
