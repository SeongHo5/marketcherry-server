package com.cherrydev.cherrymarketbe.server.application.security;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;


@Slf4j
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", HttpStatus.UNAUTHORIZED.value());
        responseJson.put("message", "인증이 필요한 서비스입니다.");

        response.getWriter().print(responseJson);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
