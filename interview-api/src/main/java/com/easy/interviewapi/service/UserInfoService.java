package com.easy.interviewapi.service;

import com.easy.interviewapi.model.UserInfo;
import com.easy.interviewapi.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public List<UserInfo> listAll() {
        return userInfoRepository.findAll();
    }

    public Optional<UserInfo> getById(long id) {
        return userInfoRepository.findById(id);
    }
}
