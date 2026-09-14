package com.easy.interviewweb.controller;

import com.easy.interviewweb.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    /**
     * #强制ASM（默认）
     * ‑Dfastjson2.creator=asm
     * #强制lambda
     * ‑Dfastjson2.creator=lambda
     * #强制反射
     * ‑Dfastjson2.creator=reflect
     * */
    @GetMapping("/fastjson")
    public String fastjson() {
        String result = employeeService.fastjson();
        logger.info("record log message: {}",result);
        return result;
    }

    @GetMapping("/jackson")
    public String jackson(){
        return  employeeService.jackson();
    }

    @GetMapping("/protobuf")
    public String protobuf(){
        return employeeService.protobuf();
    }
}
