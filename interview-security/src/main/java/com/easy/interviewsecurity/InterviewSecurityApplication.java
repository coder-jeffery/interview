package com.easy.interviewsecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class InterviewSecurityApplication {

    public static void main(String[] args) {
        long start  = System.currentTimeMillis();
        log.info("interview security >>>>");
        SpringApplication.run(InterviewSecurityApplication.class, args);
        log.info("interview security >>> use times: {}", System.currentTimeMillis() - start);
    }
}
