package com.cherrydev.cherrymarketbe.server.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import com.cherrydev.cherrymarketbe.server.application.security.CustomAccessDeniedHandler;
import com.cherrydev.cherrymarketbe.server.application.security.CustomAuthEntryPoint;
import com.cherrydev.cherrymarketbe.server.application.security.JwtAuthFilter;
import com.cherrydev.cherrymarketbe.server.application.security.RateLimitFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    protected AuthorizationManager<RequestAuthorizationContext> localHostOnly = (auth, context) -> {
        String ip = context.getRequest().getRemoteAddr();
        boolean isLocalHost = ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1");
        return new AuthorizationDecision(isLocalHost);
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(("/api/**")).permitAll()
                        .requestMatchers("/prometheus").access(localHostOnly)
                        .anyRequest().denyAll()
                )
                .addFilterBefore(new JwtAuthFilter(jwtProvider, redisService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RateLimitFilter(redisService), JwtAuthFilter.class)
                .exceptionHandling(exception -> exception.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new CustomAuthEntryPoint()))
                .build();
    }

    /**
     * CORS(Cross-Origin Resource Sharing) 설정
     */
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


}
