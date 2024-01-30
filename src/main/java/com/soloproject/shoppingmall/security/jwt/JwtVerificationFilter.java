package com.soloproject.shoppingmall.security.jwt;

import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.redis.RedisUtil;
import com.soloproject.shoppingmall.security.CustomAuthorityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

// jwt 검증
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        } catch (SignatureException e) {
            request.setAttribute("exception", e);
        } catch (ExpiredJwtException e) {
            // 리프레시 토큰 검증
            Map<String, Object> claims = verifyRefreshToken(request);
            // 검증에 통과하면
            if (claims != null) {

                String email = (String) claims.get("email");
                Date actExpirationTime = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
                String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

                String newAccessToken = "Bearer " + jwtTokenizer.generateAccessToken(claims, email, actExpirationTime, base64EncodedSecretKey);
                redisUtil.set("AccessToken : " + email, newAccessToken, jwtTokenizer.getAccessTokenExpirationMinutes());

                setAuthenticationToContext(claims);

                response.setHeader("Authorization", newAccessToken);
            } else {
                // 검증에 실패했을 경우
                request.setAttribute("만료된 토큰입니다.", e);
            }
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");

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

    public Map<String, Object> verifyRefreshToken(HttpServletRequest request) {

        String refreshToken = request.getHeader("Refresh");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey).getBody();

        String redisRefreshToken = (String) redisUtil.get("RefreshToken : " + claims.get("email"));
        if (refreshToken.equals(redisRefreshToken)) {
            return claims;
        } else throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {

        String email = (String) claims.get("email");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
