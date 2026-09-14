package com.easy.interviewexcludetomcat;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class InterviewExcludeTomcatApp extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(InterviewExcludeTomcatApp.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(InterviewExcludeTomcatApp.class, args);
    }
}

