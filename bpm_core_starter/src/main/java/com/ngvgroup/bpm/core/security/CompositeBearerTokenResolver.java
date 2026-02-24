package com.ngvgroup.bpm.core.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.List;

/**
 * Composite resolver:
 * - thử resolve token theo thứ tự danh sách delegates
 * - resolver nào trả token đầu tiên -> dùng token đó
 */
public record CompositeBearerTokenResolver(List<BearerTokenResolver> delegates) implements BearerTokenResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        for (BearerTokenResolver r : delegates) {
            String token = r.resolve(request);
            if (token != null && !token.isBlank()) {
                return token;
            }
        }
        return null;
    }
}
