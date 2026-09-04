package com.easy.interviewsecurity.controller;

import com.easy.interviewsecurity.config.AuthorizationServerConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OidcCallbackController {

    @GetMapping("/oidc/callback")
    public Map<String, Object> callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            @RequestParam(required = false, name = "error_description") String errorDescription) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", code);
        body.put("state", state);
        body.put("error", error);
        body.put("error_description", errorDescription);
        body.put("tokenEndpoint", AuthorizationServerConfig.ISSUER + "/oauth2/token");
        body.put("hint", "POST /oauth2/token，grant_type=authorization_code，Basic oidc-client:secret，再带上 code 和 redirect_uri");
        return body;
    }
}
