package com.easy.interviewweb.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.easy.interviewweb.entity.Employee;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong atomicLong = new AtomicLong();

    public String fastjson() {
        for (int i = 0; i < 10; i++) {
            System.out.println("打印:AtomicLong" + atomicLong.getAndIncrement());
        }
        var data = employee();
        //序列化
        String result = JSON.toJSONString(data);
        //反序列化
        Employee employee = JSONObject.parseObject(result, Employee.class);
        return JSON.toJSONString(employee);
    }

    public String jackson() {
        var data = employee();
        var result = objectMapper.writeValueAsString(data);
        var regionalData = objectMapper.readValue(result, Employee.class);
        return result;
    }

    public Employee employee() {
        return Employee.builder()
                .id(atomicLong.getAndIncrement())
                .name("Jeffery")
                .email("cj10840@citi.com")
                .job("software engineer")
                .address("Shanghai")
                .nickname("jeff")
                .salary(40100)
                .birthday(LocalDateTime.of(1992, 06, 22, 00, 00, 00))
                .build();
    }

}
