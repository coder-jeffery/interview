package com.easy.interviewgateway;

import com.easy.interviewgateway.bean.NacosBean;
import com.easy.interviewgateway.config.NacosConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 路由转发、鉴权、限流、熔断、日志、灰度发布、统一跨域
 * */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(NacosConfig.class)
@Slf4j
public class InterviewGatewayApplication {


    @Autowired
    NacosBean nacosBean;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        log.info("interview gateway start >>>");
        SpringApplication.run(InterviewGatewayApplication.class, args);
        log.info("interview gateway end >>> use times:{}", System.currentTimeMillis() - start);
    }
}
