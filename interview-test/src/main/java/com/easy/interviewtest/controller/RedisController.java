package com.easy.interviewtest.controller;

import com.easy.interviewtest.service.OrderService;
import com.easy.interviewtest.service.PocService;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    OrderService orderService;
    @Autowired
    PocService pocService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("redis")
    public void redis() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        orderService.pay(id);
    }

    @GetMapping("test")
    public void test() {
        String id = UUID.randomUUID().toString();
        pocService.pocPay(id);
    }

    /**
     * Uses RedisTemplate to SET/GET a probe key so you can confirm the instance is writable.
     */
    @GetMapping("redis/ping")
    public Map<String, Object> ping() {
        String key = "probe:redis-template";
        String value = "ok-" + Instant.now();
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(5));
        String read = redisTemplate.opsForValue().get(key);
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ok", value.equals(read));
        result.put("key", key);
        result.put("written", value);
        result.put("read", read);
        result.put("ttlSeconds", ttl);
        return result;
    }

    @GetMapping("redis/get")
    public Map<String, Object> get(@RequestParam(defaultValue = "probe:redis-template") String key) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("key", key);
        result.put("value", redisTemplate.opsForValue().get(key));
        result.put("ttlSeconds", redisTemplate.getExpire(key, TimeUnit.SECONDS));
        return result;
    }
}
