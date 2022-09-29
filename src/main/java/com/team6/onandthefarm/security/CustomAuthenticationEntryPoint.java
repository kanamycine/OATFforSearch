package com.team6.onandthefarm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.onandthefarm.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error("CustomAuthenticationEntryPoint - 토큰 정보가 존재하지 않습니다");
        ObjectMapper objectMapper = new ObjectMapper();

        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        BaseResponse response = BaseResponse.builder().httpStatus(HttpStatus.UNAUTHORIZED).message("토큰 정보가 존재하지 않습니다").build();

        PrintWriter out = httpServletResponse.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(response);
        out.print(jsonResponse);
    }
}