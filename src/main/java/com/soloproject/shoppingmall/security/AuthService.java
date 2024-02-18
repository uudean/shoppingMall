package com.soloproject.shoppingmall.security;

import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.security.jwt.JwtTokenizer;
import com.soloproject.shoppingmall.security.jwt.JwtVerificationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtTokenizer jwtTokenizer;
    private final JwtVerificationFilter jwtVerificationFilter;

    public boolean verify(HttpServletRequest request) {

        Map<String, Object> claims = jwtVerificationFilter.verifyJws(request);

        if (!claims.isEmpty()) {
            return true;
        } else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
    }


    public String reissue(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> claims = jwtVerificationFilter.verifyRefreshToken(request);
        if (claims != null) {

            String email = (String) claims.get("username");
            Date actExpirationTime = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

            String newAccessToken = jwtTokenizer.generateAccessToken(claims, email, actExpirationTime, base64EncodedSecretKey);

            response.setHeader("Authorization", "Bearer " + newAccessToken);
            return newAccessToken;

        } else throw new Exception();
    }
}



