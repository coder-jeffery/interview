package com.easy.interviewgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class NacosConfig {
    private String title;
    private Integer timeout;

    // getter setter
}
