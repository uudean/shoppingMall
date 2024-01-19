package com.soloproject.shoppingmall.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.redis.RedisUtil;
import com.soloproject.shoppingmall.security.LoginDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final RedisUtil redisUtil;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication authResult) throws IOException, ServletException {

        Member member = (Member) authResult.getPrincipal();

        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        response.setHeader("Authorization","Bearer "+accessToken);
        response.setHeader("Refresh",refreshToken);

        LoginDto.Response loginResponse = new LoginDto.Response(member.getMemberId(), member.getEmail(),"Bearer : "+accessToken);

        Gson gson = new Gson();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(gson.toJson(loginResponse));

        this.getSuccessHandler().onAuthenticationSuccess(request,response,authResult);
    }

    private String delegateAccessToken(Member member) {

        Map<String , Object> claims = new HashMap<>();

        claims.put("memberId",member.getMemberId());
        claims.put("email",member.getEmail());
        claims.put("roles",member.getRoles());

        String subject = member.getEmail();

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims,subject,expiration,base64EncodedSecretKey);

        redisUtil.set("AccessToken : "+member.getEmail(),accessToken,jwtTokenizer.getAccessTokenExpirationMinutes());

        return accessToken;
    }

    private String delegateRefreshToken(Member member) {

        Map<String ,Object> claims = new HashMap<>();

        claims.put("memberId",member.getMemberId());
        claims.put("email",member.getEmail());
        claims.put("roles",member.getRoles());

        String subject = member.getEmail();

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(claims,subject,expiration,base64EncodedSecretKey);

        redisUtil.set("RefreshToken : "+member.getEmail(),refreshToken, jwtTokenizer.getRefreshTokenExpirationMinutes());

        return refreshToken;
    }
}
