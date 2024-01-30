package com.soloproject.shoppingmall.security.handler;

import com.soloproject.shoppingmall.security.CustomAuthorityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("# 로그인에 성공하였습니다!");

//        String uri = createURI()
    }

//    private URI createURI(String accessToken,String refreshToken){
//        MultiValueMap<String,String > queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("accessToken",accessToken);
//        queryParams.add("refreshToken",refreshToken);
//
//        return UriComponentsBuilder
//                .newInstance()
//                .scheme("http")
//                .host("")
//    }
}
