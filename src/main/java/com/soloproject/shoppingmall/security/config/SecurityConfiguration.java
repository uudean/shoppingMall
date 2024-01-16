package com.soloproject.shoppingmall.security.config;

import com.soloproject.shoppingmall.redis.RedisUtil;
import com.soloproject.shoppingmall.security.CustomAuthorityUtils;
import com.soloproject.shoppingmall.security.handler.MemberAccessDeniedHandler;
import com.soloproject.shoppingmall.security.handler.MemberAuthenticationEntryPoint;
import com.soloproject.shoppingmall.security.handler.MemberAuthenticationFailureHandler;
import com.soloproject.shoppingmall.security.handler.MemberAuthenticationSuccessHandler;
import com.soloproject.shoppingmall.security.jwt.JwtAuthenticationFilter;
import com.soloproject.shoppingmall.security.jwt.JwtTokenizer;
import com.soloproject.shoppingmall.security.jwt.JwtVerificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final CustomAuthorityUtils authorityUtils;
    private final RedisUtil redisUtil;
    private final JwtTokenizer jwtTokenizer;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/member/signup/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/member/email/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/member/**")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/order/**")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/cart/**")).authenticated()
                        .anyRequest().permitAll())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                        .accessDeniedHandler(new MemberAccessDeniedHandler()))
                .apply(new CustomFilterConfigurer());

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer, redisUtil);
            authenticationFilter.setFilterProcessesUrl("/login");
            authenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            authenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer,authorityUtils,redisUtil);

            builder.addFilter(authenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }
}
