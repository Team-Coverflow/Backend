package com.coverflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class CoverflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoverflowApplication.class, args);
    }

}
