package com.ngv.aia_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello World! API is working!";
    }
    
    @GetMapping("/auth")
    public Object checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return "Authenticated: " + auth.getName() + ", Authorities: " + auth.getAuthorities();
        }
        return "Not authenticated";
    }
    
    @GetMapping("/debug")
    public Map<String, Object> debug(HttpServletRequest request) {
        Map<String, Object> debug = new HashMap<>();
        debug.put("method", request.getMethod());
        debug.put("requestURL", request.getRequestURL().toString());
        debug.put("contextPath", request.getContextPath());
        debug.put("servletPath", request.getServletPath());
        debug.put("pathInfo", request.getPathInfo());
        debug.put("remoteAddr", request.getRemoteAddr());
        
        // Headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        debug.put("headers", headers);
        
        return debug;
    }
}
