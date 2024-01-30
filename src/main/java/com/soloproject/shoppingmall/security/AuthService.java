package com.soloproject.shoppingmall.security;

import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.redis.RedisUtil;
import com.soloproject.shoppingmall.security.jwt.JwtTokenizer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final RedisUtil redisUtil;
    private final JwtTokenizer jwtTokenizer;

    public String reissue(HttpServletRequest request, HttpServletResponse response) throws Exception{

        Map<String,Object> claims = verifyRefreshToken(request);
        if (claims!=null) {

            String email = (String) claims.get("email");
            Date actExpirationTime = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

            String newAccessToken = jwtTokenizer.generateAccessToken(claims,email,actExpirationTime,base64EncodedSecretKey);

            redisUtil.set("AccessToken : "+email,newAccessToken, jwtTokenizer.getAccessTokenExpirationMinutes());


            response.setHeader("Authorization","Bearer "+newAccessToken);
            return newAccessToken;

        }else throw new Exception();
    }

        public Map<String, Object> verifyRefreshToken (HttpServletRequest request) throws Exception {

            String refreshToken = request.getHeader("Refresh");
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            Map<String, Object> claims = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey).getBody();

            String redisRefreshToken = (String) redisUtil.get("RefreshToken : " + claims.get("email"));
            if (refreshToken.equals(redisRefreshToken)) {
                return claims;
            } else throw new Exception();
        }

    public Map<String, Object> verifyJws(HttpServletRequest request) {

        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        // redis 에 저장 되어 있는 토큰과 헤더에 포함된 토큰이 일치 하는지 검증
        String redisJwt = (String) redisUtil.get("AccessToken : " + claims.get("email"));
        if (jws.equals(redisJwt)) {
            return claims;
        } else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
    }
    }



