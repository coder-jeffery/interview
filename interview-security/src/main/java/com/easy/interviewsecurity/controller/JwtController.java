package com.easy.interviewsecurity.controller;

import com.easy.interviewsecurity.dto.JwtParseResponse;
import com.easy.interviewsecurity.dto.LoginDTO;
import com.easy.interviewsecurity.dto.ParseTokenRequest;
import com.easy.interviewsecurity.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class JwtController {

    private final JwtUtil jwtUtil;

    public JwtController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO dto) {
        // 业务逻辑：查询数据库，BCrypt校验账号密码
        // 校验成功生成jwt返回
        return jwtUtil.generateAccessToken(1001L, dto.getUsername());
    }

    /**
     * 由 OAuth2 资源服务器验签后，返回当前 Bearer token 中的账号信息。
     */
    @GetMapping("/jwt/me")
    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("subject", jwt.getSubject());
        body.put("issuer", jwt.getIssuer());
        body.put("audience", jwt.getAudience());
        body.put("userId", jwt.getClaim("userId"));
        body.put("username", jwt.getClaim("username"));
        body.put("preferredUsername", jwt.getClaim("preferred_username"));
        body.put("expiresAt", jwt.getExpiresAt());
        return body;
    }

    /**
     * 解析 JWT，返回签发时写入的账号信息。密码不会进入 token，因此解析结果不含密码。
     */
    @PostMapping("/jwt/parse")
    public JwtParseResponse parse(@RequestBody(required = false) ParseTokenRequest request,
                                  @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = resolveToken(request, authHeader);
        if (token == null || token.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token 不能为空");
        }
        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token 无效或已过期");
        }
        Claims claims = jwtUtil.parseClaims(token);
        JwtParseResponse response = new JwtParseResponse();
        response.setUserId(jwtUtil.getUserId(claims));
        response.setUsername(jwtUtil.getUsername(claims));
        response.setIssuedAt(claims.getIssuedAt());
        response.setExpiration(claims.getExpiration());
        return response;
    }

    private String resolveToken(ParseTokenRequest request, String authHeader) {
        if (request != null && request.getToken() != null && !request.getToken().isBlank()) {
            return request.getToken().trim();
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }
        return null;
    }
}
