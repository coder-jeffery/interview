package com.easy.interviewweb.controller;

import com.easy.interviewweb.dto.UserDTO;
import com.easy.interviewweb.service.RedisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/redis")
    public ResponseEntity<UserDTO> userInfo(@RequestParam("phone") String phone) {
        UserDTO userDTO = redisService.userInfo(phone);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/redis")
    public ResponseEntity<UserDTO> user(@Valid @RequestBody UserDTO userDto) {
        return ResponseEntity.ok(redisService.save(userDto));
    }
}
