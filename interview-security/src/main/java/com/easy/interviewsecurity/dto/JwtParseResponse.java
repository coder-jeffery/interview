package com.easy.interviewsecurity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class JwtParseResponse {
    private Long userId;
    private String username;
    private Date issuedAt;
    private Date expiration;
}
