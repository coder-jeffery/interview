package com.easy.interviewweb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient //开启Nacos服务注册发现
@EnableFeignClients //开启Feign扫描
@Slf4j
public class InterviewWebApplication {

	public static void main(String[] args) {
		log.info("app start >>>");
		long start = System.currentTimeMillis();
		SpringApplication.run(InterviewWebApplication.class, args);
		log.info("app end <<< , use times :{}", System.currentTimeMillis() - start);
	}
}
