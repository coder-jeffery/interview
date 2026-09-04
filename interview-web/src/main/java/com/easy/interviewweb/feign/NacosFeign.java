package com.easy.interviewweb.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("interview-gateway")
public interface NacosFeign {

    @GetMapping("/api/hello")
    String hello();
}
