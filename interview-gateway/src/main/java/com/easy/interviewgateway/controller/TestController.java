package com.easy.interviewgateway.controller;

import com.easy.interviewgateway.bean.NacosBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    NacosBean nacosBean;

    @GetMapping("/test")
    public String test(){
        return "打印参数：" + "响应时间:" + nacosBean.getTimeout() + " 标题:  " + nacosBean.getTitle() +" : " + nacosBean.getApp();
    }

    @GetMapping("/api/hello")
    public String hello(){
        return "来自interview-gateway实例";
    }
}
