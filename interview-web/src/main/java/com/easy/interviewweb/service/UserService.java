package com.easy.interviewweb.service;

import com.easy.interviewweb.dto.UserDTO;
import com.easy.interviewweb.entity.User;
import com.easy.interviewweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> userinfo(){
        return userRepository.findAll();
    }

    public User insertUser(UserDTO userDto){
        var entity = User.builder()
                .phone(userDto.getPhone())
                .status(userDto.getStatus())
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .password(userDto.getPassword())
                .avatar(userDto.getAvatar())
                .username(userDto.getUsername())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        return userRepository.save(entity);
    }
}
