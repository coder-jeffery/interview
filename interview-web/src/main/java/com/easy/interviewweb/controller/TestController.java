package com.easy.interviewweb.controller;

import com.easy.interviewweb.service.BizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    BizService bizService;

    @GetMapping("/index")
    public String index(){
        bizService.doBiz();
        return "hello world";
    }
}
