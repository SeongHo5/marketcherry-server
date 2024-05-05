package com.cherrydev.cherrymarketbe.server.application.security;

import com.cherrydev.cherrymarketbe.server.application.exception.AuthException;
import com.cherrydev.cherrymarketbe.server.application.common.jwt.JwtProvider;
import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.BLACKLISTED_TOKEN;
import static com.cherrydev.cherrymarketbe.server.application.exception.ExceptionStatus.INVALID_AUTH_ERROR;
import static com.cherrydev.cherrymarketbe.server.application.auth.constant.AuthConstant.BLACKLISTED_KEY_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String userToken = jwtProvider.resolveToken(request);

        // 요청에 토큰이 포함된 때만 검증 - 인증을 요구하지 않는 API에 대한 검증 방지
        if (userToken != null) {
            try {
                validateToken(userToken);
                // 토큰에서 사용자 정보를 추출하고 인증을 설정합니다.
                Claims userInfo = jwtProvider.getInfoFromToken(userToken);
                setAuthentication(userInfo.getSubject());
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_AUTH_ERROR.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtProvider.createAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void validateToken(String token) {
        checkIfTokenInBlackList(token);
        jwtProvider.validateToken(token);
    }

    private void checkIfTokenInBlackList(String token) {
        if (redisService.hasKey(BLACKLISTED_KEY_PREFIX + token)) {
            throw new AuthException(BLACKLISTED_TOKEN);
        }
    }

}
