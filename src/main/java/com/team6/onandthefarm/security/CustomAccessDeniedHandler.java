package com.team6.onandthefarm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.onandthefarm.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        log.error("CustomAccessDeniedHandler - 접근 권한 없는 유저가 접근");
        ObjectMapper objectMapper = new ObjectMapper();

        httpServletResponse.setStatus(403);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        BaseResponse response = BaseResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("접근 가능한 권한을 가지고 있지 않습니다.").build();

        PrintWriter out = httpServletResponse.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(response);
        out.print(jsonResponse);
    }
}