package com.easy.interviewweb.controller;

import com.easy.interviewweb.dto.UserDTO;
import com.easy.interviewweb.entity.User;
import com.easy.interviewweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService  userService;

    @GetMapping("/user")
    public List<User> user() {
        return userService.userinfo();
    }

    @PostMapping("/user")
    public User user(@Valid @RequestBody UserDTO userDto){
        return userService.insertUser(userDto);
    }
}
