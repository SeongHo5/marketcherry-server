package com.cherrydev.cherrymarketbe.server.application.security;

import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

import static org.apache.http.HttpStatus.SC_TOO_MANY_REQUESTS;
import static org.springframework.http.HttpHeaders.RETRY_AFTER;

/**
 * 토큰 버킷 방식으로 API 요청 제한을 하는 필터입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RedisService redisService;
    public static final String API_ENDPOINT_PREFIX = "/api";
    public static final String RATE_LIMIT_KEY_PREFIX = "RATE::LIMIT::";
    public static final int MAX_REQUESTS_PER_MINUTE = 40;
    public static final int BLOCK_TIME_IN_SECONDS = 30;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String clientIp = request.getRemoteAddr();

        if (!requestURI.startsWith(API_ENDPOINT_PREFIX)) {
            String key = RATE_LIMIT_KEY_PREFIX + clientIp;
            String currentValue = redisService.getData(key, String.class);

            if (currentValue == null) {
                redisService.setDataExpire(key, "1", Duration.ofMinutes(1));
            } else {
                int requests = Integer.parseInt(currentValue);

                if (requests >= MAX_REQUESTS_PER_MINUTE) {
                    handleRateLimitExceeded(response, clientIp);
                    return;
                }
                redisService.incrementValueByKey(key);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleRateLimitExceeded(HttpServletResponse response, String clientIp) throws IOException {
        log.warn("[RateLimitFilter] Too many requests from [{}]", clientIp);

        redisService.extendExpireTime(RATE_LIMIT_KEY_PREFIX + clientIp, BLOCK_TIME_IN_SECONDS);

        response.setStatus(SC_TOO_MANY_REQUESTS);
        response.addIntHeader(RETRY_AFTER, BLOCK_TIME_IN_SECONDS);
        response.getWriter().write("Too many requests, please try again later.");
    }
}
