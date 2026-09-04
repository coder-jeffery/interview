package com.easy.interviewsecurity.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretStr;

    @Value("${jwt.access-expire-minutes}")
    private long accessExpireMin;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成accessToken
     */
    public String generateAccessToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        long expireMs = accessExpireMin * 60 * 1000;
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + expireMs);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expireAt)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析获取载荷
     */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(Claims claims) {
        Object value = claims.get("userId");
        if (value instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    public String getUsername(Claims claims) {
        Object value = claims.get("username");
        return value == null ? null : String.valueOf(value);
    }

    /**
     * 校验token合法性
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // token过期
        } catch (MalformedJwtException e) {
            // token格式错误
        } catch (io.jsonwebtoken.security.SignatureException e) {
            // 签名错误，密钥不匹配
        } catch (Exception ignored) {
        }
        return false;
    }
}

