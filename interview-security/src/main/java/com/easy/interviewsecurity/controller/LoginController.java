package com.easy.interviewsecurity.controller;

import com.easy.interviewsecurity.dto.LoginDTO;
import com.easy.interviewsecurity.utils.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final JwtUtil jwtUtil;

    public LoginController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO dto) {
        // 业务逻辑：查询数据库，BCrypt校验账号密码
        // 校验成功生成jwt返回
        return jwtUtil.generateAccessToken(1001L, dto.getUsername());
    }
}



