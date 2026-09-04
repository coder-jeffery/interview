package com.easy.interviewweb.controller;

import com.easy.interviewweb.feign.NacosFeign;
import com.easy.interviewweb.service.BizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    BizService bizService;

    @Autowired
    NacosFeign nacosFeign;

    @GetMapping("/index")
    public String index(){
        System.out.println("exec start :" + System.currentTimeMillis());
        bizService.doAsyncTask();
        return "hello world :"+System.currentTimeMillis();
    }

    @GetMapping("/async")
    public String async(){
        System.out.println("Step 1 : async method: " + System.currentTimeMillis());
        bizService.execOrder();
        return "Step 1 : create order success! time:"+System.currentTimeMillis();
    }

    @GetMapping("/nacos")
    public String hello(){
        return nacosFeign.hello();
    }
}
