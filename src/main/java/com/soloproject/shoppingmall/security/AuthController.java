package com.soloproject.shoppingmall.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public String reissue(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessToken = authService.reissue(request, response);
        return accessToken;
    }

    @PostMapping("/verify")
    public Boolean verifyJws(HttpServletRequest request) {
        Map<String, Object> claims = authService.verifyJws(request);
        return claims != null;
    }
}
