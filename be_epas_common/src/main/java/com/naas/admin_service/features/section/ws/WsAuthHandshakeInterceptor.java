package com.naas.admin_service.features.section.ws;

import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetSocketAddress;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WsAuthHandshakeInterceptor implements HandshakeInterceptor {

    private final MultitenancyProperties mtProps;

    @Override
    public boolean beforeHandshake(
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response,
            @NotNull WebSocketHandler wsHandler,
            @NotNull Map<String, Object> attributes
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jat)) {
            log.warn("WS handshake rejected: missing JwtAuthenticationToken in SecurityContext");
            return false;
        }

        Jwt jwt = jat.getToken();

        // ----- username -----
        String username = firstNonBlank(
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("user_name"),
                jwt.getSubject()
        );
        if (username == null || username.isBlank()) {
            log.warn("WS handshake rejected: cannot extract username from verified JWT");
            return false;
        }

        // ----- tenant -----
        String claimKey = mtProps.getTenantClaim();
        if (claimKey == null || claimKey.isBlank()) claimKey = "tenant_id";

        String tenantId = resolveTenantId(jwt, claimKey);

        if (mtProps.isEnabled() && (tenantId == null || tenantId.isBlank())) {
            log.warn("WS handshake rejected: multi enabled but missing tenant. claimKey={}, claimsKeys={}",
                    claimKey, jwt.getClaims().keySet());
            log.warn("WS debug organization={}", jwt.getClaims().get("organization"));
            log.warn("WS debug organizations={}", jwt.getClaims().get("organizations"));
            return false;
        }

        String ip = resolveClientIp(request);
        String userAgent = safeUserAgent(request);

        attributes.put("username", username);
        attributes.put("tenantId", tenantId);
        attributes.put("clientIp", ip);
        attributes.put("userAgent", userAgent);
        attributes.put("driverId", resolveDriverIdFromQuery(request));

        log.info("WS handshake OK: user={} tenant={} ip={}", username, tenantId, ip);
        return true;
    }

    @Override
    public void afterHandshake(
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response,
            @NotNull WebSocketHandler wsHandler,
            Exception exception
    ) { /* no-op */ }

    // =========================
    // Tenant resolution helpers
    // =========================

    private String resolveTenantId(Jwt jwt, String claimKey) {
        // 1) top-level claim dạng string
        String v = jwt.getClaimAsString(claimKey);
        if (isNotBlank(v)) return v.trim();

        // 2) top-level claim dạng list/object
        Object raw = jwt.getClaims().get(claimKey);
        String fromRaw = normalizeClaimValue(raw);
        if (isNotBlank(fromRaw)) return fromRaw;

        // 3) organization (single) - hỗ trợ nhiều format
        Object orgObj = jwt.getClaims().get("organization");
        String fromOrg = extractTenantFromOrganizationClaim(orgObj, claimKey);
        if (isNotBlank(fromOrg)) return fromOrg;

        // 4) organizations (many) có thể là list hoặc map
        Object orgsObj = jwt.getClaims().get("organizations");
        String fromOrgs = extractTenantFromOrganizationsClaim(orgsObj, claimKey);
        if (isNotBlank(fromOrgs)) return fromOrgs;

        return null;
    }

    /**
     * Hỗ trợ các dạng:
     * A) { "attributes": { "tenant_id": ["EPAS_DEV"] } }
     * B) { "tenant_id": ["EPAS_DEV"] }
     * C) { "epas_dev": { "tenant_id": ["EPAS_DEV"] } }
     */
    private String extractTenantFromOrganizationClaim(Object orgObj, String claimKey) {
        Map<?, ?> orgMap = asMap(orgObj);
        if (orgMap == null) return null;

        // A) org.attributes.<claimKey>
        String fromAttrs = readFromAttributes(orgMap, claimKey);
        if (isNotBlank(fromAttrs)) return fromAttrs;

        // B) org.<claimKey>
        String direct = readClaim(orgMap, claimKey);
        if (isNotBlank(direct)) return direct;

        // C) org.<dynamicKey>.<claimKey> or org.<dynamicKey>.attributes.<claimKey>
        return findInNestedMaps(orgMap, claimKey);
    }

    private String findInNestedMaps(Map<?, ?> orgMap, String claimKey) {
        for (Object v : orgMap.values()) {
            Map<?, ?> inner = asMap(v);
            if (inner == null) continue;

            String t = readClaim(inner, claimKey);
            if (isNotBlank(t)) return t;

            t = readFromAttributes(inner, claimKey);
            if (isNotBlank(t)) return t;
        }
        return null;
    }

    private String readFromAttributes(Map<?, ?> map, String claimKey) {
        Map<?, ?> attrs = asMap(map.get("attributes"));
        return (attrs == null) ? null : readClaim(attrs, claimKey);
    }

    private String readClaim(Map<?, ?> map, String claimKey) {
        return normalizeClaimValue(map.get(claimKey));
    }

    private Map<?, ?> asMap(Object obj) {
        return (obj instanceof Map<?, ?> m) ? m : null;
    }

    /**
     * organizations claim có thể là:
     * - List<Map> (mỗi phần tử giống org)
     * - Map (kiểu giống organization)
     */
    private String extractTenantFromOrganizationsClaim(Object orgsObj, String claimKey) {
        if (orgsObj == null) return null;

        if (orgsObj instanceof Iterable<?> it) {
            for (Object o : it) {
                String t = extractTenantFromOrganizationClaim(o, claimKey);
                if (isNotBlank(t)) return t;
            }
            return null;
        }

        if (orgsObj instanceof Map<?, ?>) {
            return extractTenantFromOrganizationClaim(orgsObj, claimKey);
        }

        return null;
    }

    private String normalizeClaimValue(Object raw) {
        if (raw == null) return null;

        if (raw instanceof String s) {
            s = s.trim();
            return s.isEmpty() ? null : s;
        }

        if (raw instanceof Iterable<?> it) {
            for (Object o : it) {
                if (o == null) continue;
                String s = o.toString().trim();
                if (!s.isEmpty()) return s;
            }
            return null;
        }

        String s = raw.toString().trim();
        return s.isEmpty() ? null : s;
    }

    private String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private boolean isNotBlank(String s) {
        return s != null && !s.isBlank();
    }

    // =========================
    // Existing helpers
    // =========================

    private String resolveDriverIdFromQuery(ServerHttpRequest request) {
        MultiValueMap<String, String> params =
                UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String driverId = params.getFirst("driverId");
        return (driverId == null || driverId.isBlank()) ? null : driverId.trim();
    }

    private String resolveClientIp(ServerHttpRequest request) {
        String xff = request.getHeaders().getFirst("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();

        String xri = request.getHeaders().getFirst("X-Real-IP");
        if (xri != null && !xri.isBlank()) return xri.trim();

        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress.getAddress() != null) {
            return remoteAddress.getAddress().getHostAddress();
        }
        return "UNKNOWN";
    }

    private String safeUserAgent(ServerHttpRequest request) {
        String v = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
        return v == null ? "" : v;
    }
}
