package com.ngvgroup.bpm.core.security.ws;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import java.util.Collections;
import java.util.Enumeration;

public class WsProtocolBearerTokenResolver implements BearerTokenResolver {

    private final DefaultBearerTokenResolver delegate = new DefaultBearerTokenResolver();
    private final String wsPathPrefix;

    public WsProtocolBearerTokenResolver() {
        this.wsPathPrefix = "/api/ws"; // ví dụ "/api/ws"
    }

    @Override
    public String resolve(HttpServletRequest request) {
        // 1) nếu Authorization có sẵn thì dùng bình thường
        String token = delegate.resolve(request);
        if (token != null) return token;

        // 2) chỉ áp dụng cho WS handshake + đúng path
        if (!isWebSocketHandshake(request)) return null;

        String uri = request.getRequestURI();
        if (uri == null || !uri.startsWith(wsPathPrefix)) return null;

        // 3) lấy từ Sec-WebSocket-Protocol: ví dụ "jwt", "jwt.<TOKEN>"
        String fromProtocol = resolveFromSecWebSocketProtocol(request);
        if (fromProtocol != null && !fromProtocol.isBlank()) return fromProtocol;

        // (optional) nếu bạn vẫn cho phép query token thì có thể fallback:
        // return request.getParameter("token");

        return null;
    }

    private boolean isWebSocketHandshake(HttpServletRequest request) {
        String upgrade = request.getHeader("Upgrade");
        if ("websocket".equalsIgnoreCase(upgrade)) return true;
        return request.getHeader("Sec-WebSocket-Key") != null;
    }

    private String resolveFromSecWebSocketProtocol(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Sec-WebSocket-Protocol");
        if (headers == null) return null;

        for (String v : Collections.list(headers)) {
            if (v == null) continue;
            for (String part : v.split(",")) {
                String p = part.trim();
                if (p.startsWith("jwt.")) {
                    return p.substring(4); // lấy token sau "jwt."
                }
            }
        }
        return null;
    }
}