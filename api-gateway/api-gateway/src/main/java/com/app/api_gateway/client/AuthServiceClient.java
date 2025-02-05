package com.app.api_gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient( name = "loginService", url = "${loginService_URL}")
public interface AuthServiceClient {
    @GetMapping("api/auth/validate")
    public ResponseEntity<String> validateToken(@CookieValue(name = "JWT")String token);
}
