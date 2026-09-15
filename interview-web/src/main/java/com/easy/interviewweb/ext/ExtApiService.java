package com.easy.interviewweb.ext;

import com.easy.interviewweb.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExtApiService {

    @Value("${ext.api.security}")
    private String extApiSecurity;

    private final RestTemplate restTemplate;

    public ExtApiService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String login(LoginDTO loginDTO){
        return restTemplate.postForEntity(extApiSecurity, loginDTO, String.class).getBody();
    }
}
