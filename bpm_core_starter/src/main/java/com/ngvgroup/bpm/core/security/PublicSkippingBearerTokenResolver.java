package com.ngvgroup.bpm.core.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * PublicSkipping resolver:
 * - Nếu request thuộc public endpoints (permitAll) -> coi như không có token
 * => public endpoint có token sai/expired cũng không bị 401
 * - Nếu không phải public -> delegate.resolve(request)
 * <p>
 * Lưu ý:
 * - Đây là "behavior" đặc thù của hệ thống bạn (public không bị 401 vì token sai)
 * - Đặt trong resolver giúp xử lý sớm, trước khi BearerTokenAuthenticationFilter validate token.
 */
public record PublicSkippingBearerTokenResolver(RequestMatcher publicMatcher,
                                                BearerTokenResolver delegate) implements BearerTokenResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        if (publicMatcher != null && publicMatcher.matches(request)) {
            return null;
        }
        return delegate.resolve(request);
    }
}
