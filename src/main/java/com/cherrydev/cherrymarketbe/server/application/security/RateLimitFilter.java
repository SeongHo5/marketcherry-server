package com.cherrydev.cherrymarketbe.server.application.security;

import static org.apache.http.HttpStatus.SC_TOO_MANY_REQUESTS;
import static org.springframework.http.HttpHeaders.RETRY_AFTER;

import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** 토큰 버킷 방식으로 API 요청 제한을 하는 필터입니다. */
@Slf4j(topic = "RateLimitFilter")
@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

  private static final String API_ENDPOINT_PREFIX = "/api";
  private static final String RATE_LIMIT_KEY_PREFIX = "RATE::LIMIT::";
  private static final int MAX_REQUESTS_PER_MINUTE = 40;
  private static final int BLOCK_TIME_IN_SECONDS = 30;

  private final RedisService redisService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (!request.getRequestURI().startsWith(API_ENDPOINT_PREFIX)) {
      doRateLimiting(request.getRemoteAddr(), response);
    }
    filterChain.doFilter(request, response);
  }

  private void doRateLimiting(String clientIp, HttpServletResponse response) throws IOException {
    String key = RATE_LIMIT_KEY_PREFIX + clientIp;
    String currentValue = redisService.getData(key, String.class);

    if (currentValue == null) {
      initializeRequestCount(key);
    } else {
      processCurrentRateLimitCount(key, currentValue, response, clientIp);
    }
  }

  private void initializeRequestCount(String key) {
    redisService.setDataExpire(key, "1", Duration.ofMinutes(1));
  }

  private void processCurrentRateLimitCount(
      String key, String currentValue, HttpServletResponse response, String clientIp)
      throws IOException {
    int requests = Integer.parseInt(currentValue);

    if (requests >= MAX_REQUESTS_PER_MINUTE) {
      handleRateLimitExceeded(response, clientIp);
    } else {
      redisService.incrementValueByKey(key);
    }
  }

  private void handleRateLimitExceeded(HttpServletResponse response, String clientIp)
      throws IOException {
    log.warn("Too many requests from [{}]", clientIp);

    redisService.extendExpireTime(RATE_LIMIT_KEY_PREFIX + clientIp, BLOCK_TIME_IN_SECONDS);

    response.setStatus(SC_TOO_MANY_REQUESTS);
    response.addIntHeader(RETRY_AFTER, BLOCK_TIME_IN_SECONDS);
    response.getWriter().write("Too many requests, please try again later.");
    response.getWriter().flush();
    response.getWriter().close();
  }
}
