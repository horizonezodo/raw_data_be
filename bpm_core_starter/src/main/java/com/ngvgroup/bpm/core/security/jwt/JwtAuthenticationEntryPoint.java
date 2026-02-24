package com.ngvgroup.bpm.core.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        log.error("JWT Authentication failed: {}", authException.getMessage());
        
        // Sử dụng ErrorCode chung cho tất cả JWT authentication errors
        ErrorCode errorCode = ErrorCode.JWT_AUTHENTICATION_FAILED;
        
        // Tạo response JSON
        ResponseData<Void> responseData = new ResponseData<>(
            errorCode.getCode(),
            errorCode.getMessage(),
            null
        );
        
        String jsonResponse = objectMapper.writeValueAsString(responseData);
        
        // Set response headers
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        // Write response body
        response.getWriter().write(jsonResponse);
    }
} 