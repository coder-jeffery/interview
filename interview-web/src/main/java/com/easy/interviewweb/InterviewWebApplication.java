package com.easy.interviewweb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class InterviewWebApplication {

	public static void main(String[] args) {
		log.info("app start >>>");
		long start = System.currentTimeMillis();
		SpringApplication.run(InterviewWebApplication.class, args);
		log.info("app end <<< , use times :{}", System.currentTimeMillis() - start);
	}

}
