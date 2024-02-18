package com.soloproject.shoppingmall.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity verify(HttpServletRequest request){
        boolean isVerify = authService.verify(request);

        return new ResponseEntity<>(isVerify, HttpStatus.OK);
    }
}
