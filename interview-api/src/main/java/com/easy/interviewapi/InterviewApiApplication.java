package com.easy.interviewapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class InterviewApiApplication {
    public static void main(String[] args) {
        long start  = System.currentTimeMillis();
        log.info("InterviewApiApplication start");
        SpringApplication.run(InterviewApiApplication.class, args);
        log.info("InterviewApiApplication end, use times: " + (System.currentTimeMillis() - start) + " s");
    }
}
