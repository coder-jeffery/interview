package com.easy.interviewweb.controller;

import com.easy.interviewweb.dto.UserDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RestController
public class RedisController {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/redis")
    public Object userInfo(@RequestParam("phone") String phone){
        return  stringRedisTemplate.opsForValue().get(phone);
    }


    @PostMapping("/redis")
    public void user(@RequestBody UserDTO userDto){
        long start  = System.currentTimeMillis();
        log.info("redis set start");
        stringRedisTemplate.opsForValue().set(!StringUtils.isEmpty(userDto.getPhone()) ? userDto.getPhone() : "temp_key", objectMapper.writeValueAsString(userDto));
        log.info("redis set end , use time: {}", System.currentTimeMillis() - start);
    }
}
