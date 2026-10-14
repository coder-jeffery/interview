package com.easy.interviewtest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class InterviewTestApplication {

    public static void main(String[] args) {
        log.info("start >>>>");
        long start  = System.currentTimeMillis();
        SpringApplication.run(InterviewTestApplication.class, args);
        log.info("end >>>, use times:{}", System.currentTimeMillis() - start);
    }

}
