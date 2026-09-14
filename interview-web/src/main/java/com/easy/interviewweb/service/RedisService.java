package com.easy.interviewweb.service;

import com.easy.interviewweb.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class RedisService {

    private static final String USER_PHONE_KEY_PREFIX = "user:phone:";
    private static final long CACHE_TTL_MINUTES = 30;
    private static final int CACHE_TTL_JITTER_MINUTES = 10;

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public UserDTO userInfo(String phone){
        if (!StringUtils.hasText(phone)) {
//            return ResponseEntity.badRequest().build();
        }
        String json = stringRedisTemplate.opsForValue().get(userPhoneKey(phone));
        if (!StringUtils.hasText(json)) {
//            return ResponseEntity.notFound().build();
        }
        return objectMapper.readValue(json, UserDTO.class);
    }

    public UserDTO save(UserDTO userDto){
        UserDTO cached = userDto.copyForCache();
        cached.setPhone(cached.getPhone().trim());
        long ttlMinutes = CACHE_TTL_MINUTES + ThreadLocalRandom.current().nextInt(CACHE_TTL_JITTER_MINUTES + 1);
        stringRedisTemplate.opsForValue().set(
                userPhoneKey(cached.getPhone()),
                objectMapper.writeValueAsString(cached),
                Duration.ofMinutes(ttlMinutes)
        );
        return cached;
    }

    private static String userPhoneKey(String phone) {
        return USER_PHONE_KEY_PREFIX + phone.trim();
    }
}
