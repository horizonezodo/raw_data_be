package com.ngvgroup.bpm.core.security;

import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.OrganizationContext;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * TenantJwtFilter (JWT-first)
 * - PUBLIC: tenant bắt buộc từ HEADER (X-Tenant-Id) để route DB
 * - NON-PUBLIC:
 *    + User token: tenant từ JWT (tenantClaim) hoặc từ organization claim (nếu org-mode)
 *    + S2S token: tenant từ HEADER (X-Tenant-Id). Org alias từ header (X-Org-Alias) nếu có.
 * Lưu ý đa môi trường:
 * - Nếu Keycloak cũ KHÔNG có Organizations: user token sẽ không có org claim -> tự fallback sang tenantClaim/issuer mapping.
 * - Với S2S: nếu không gửi X-Org-Alias thì filter vẫn cho qua (orgAlias = null). Service phía trên nên có fallback org-mode.
 */
@Slf4j
public class TenantJwtFilter extends OncePerRequestFilter {

    /** Claim tenant_id trong JWT (default: tenant_id) */
    private final String tenantClaim;

    /** Header tenant (default: X-Tenant-Id) */
    private final String tenantHeader;

    /** Registry chứa config tenant->datasource và issuer->tenant */
    private final JdbcTenantDbConfigRegistry registry;

    /** Matcher để nhận biết public endpoints */
    private final RequestMatcher publicMatcher;

    /** Org mode enabled theo config */
    private final boolean orgEnabledConfig;

    /** Organization claim name (default: organization) */
    private final String orgClaimName;

    /** Header để chọn org alias khi cần (default: X-Org-Alias) */
    private final String orgSelectHeader;

    /** Key trong org-attributes để lấy tenant (default: tenant_id) */
    private final String orgTenantAttribute;

    public TenantJwtFilter(
            MultitenancyProperties mtProps,
            JdbcTenantDbConfigRegistry registry,
            RequestMatcher publicMatcher
    ) {
        this.registry = registry;
        this.publicMatcher = publicMatcher;

        this.tenantClaim = (mtProps != null && mtProps.getTenantClaim() != null && !mtProps.getTenantClaim().isBlank())
                ? mtProps.getTenantClaim().trim()
                : "tenant_id";

        this.tenantHeader = (mtProps != null && mtProps.getTenantHeader() != null && !mtProps.getTenantHeader().isBlank())
                ? mtProps.getTenantHeader().trim()
                : "X-Tenant-Id";

        MultitenancyProperties.Organizations orgCfg = (mtProps != null) ? mtProps.getOrganizations() : null;
        this.orgEnabledConfig = orgCfg != null && orgCfg.isEnabled();

        this.orgClaimName = (orgCfg != null && orgCfg.getClaim() != null && !orgCfg.getClaim().isBlank())
                ? orgCfg.getClaim().trim()
                : "organization";

        this.orgSelectHeader = (orgCfg != null && orgCfg.getSelectHeader() != null && !orgCfg.getSelectHeader().isBlank())
                ? orgCfg.getSelectHeader().trim()
                : "X-Org-Alias";

        this.orgTenantAttribute = (orgCfg != null && orgCfg.getTenantAttribute() != null && !orgCfg.getTenantAttribute().isBlank())
                ? orgCfg.getTenantAttribute().trim()
                : "tenant_id";
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // ✅ Camunda shared: bỏ qua tenant-check
            if (isCamundaRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 1) PUBLIC endpoints: tenant bắt buộc từ header
            if (isPublic(request)) {
                handlePublicRequest(request, response, filterChain);
                return;
            }

            // 2) NON-PUBLIC endpoints
            handleNonPublicRequest(request, response, filterChain);

        } finally {
            TenantContext.clear();
            OrganizationContext.clear();
        }
    }

    private boolean isPublic(HttpServletRequest request) {
        return publicMatcher != null && publicMatcher.matches(request);
    }

    private void handlePublicRequest(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws IOException, ServletException {

        String tenantId = readTenantFromHeaderOr400(request, response);
        if (tenantId == null) return;

        // ✅ master được phép đi qua dù chưa có trong COM_CFG_TENANT
        if (isNotMasterTenant(tenantId)) {
            if (registry == null || registry.find(tenantId).isEmpty()) {
                writeBadRequest(response, "Invalid tenantId in header " + tenantHeader + ": " + tenantId);
                return;
            }
        }

        TenantContext.setTenantId(tenantId);
        filterChain.doFilter(request, response);
    }

    private void handleNonPublicRequest(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws IOException, ServletException {

        Jwt jwt = getJwtFromSecurityContext();
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final boolean s2s = isServiceToServiceToken(jwt);

        String tenantId;
        String orgAlias = null;

        if (!s2s) {
            // USER token: tenant từ JWT (tenantClaim) hoặc org-claim (nếu có)
            tenantId = resolveTenantForUserToken(jwt, request, response);
            if (tenantId == null) return;

            // orgAlias có thể đã set trong resolveTenantFromOrganization()
            orgAlias = OrganizationContext.getOrgAlias();

        } else {
            // S2S token: tenant bắt buộc từ header
            tenantId = readTenantFromHeaderOr400(request, response);
            if (tenantId == null) return;

            // Org alias:
            // - Nếu service gọi theo org-mode => nên truyền header X-Org-Alias
            // - Để chạy được môi trường KC cũ (không có org), nếu thiếu header thì vẫn cho qua (orgAlias=null)
            if (orgEnabledConfig) {
                String h = request.getHeader(orgSelectHeader);
                if (h != null && !h.isBlank()) {
                    orgAlias = h.trim();
                }
            }
        }

        tenantId = tenantId.trim();

        // ✅ master được phép
        if (isNotMasterTenant(tenantId)) {
            if (registry == null || registry.find(tenantId).isEmpty()) {
                writeUnauthorized(response, "Invalid tenant: " + tenantId);
                return;
            }
        }

        TenantContext.setTenantId(tenantId);

        if (orgAlias != null && !orgAlias.isBlank()) {
            OrganizationContext.setOrgAlias(orgAlias);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveTenantForUserToken(Jwt jwt, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // orgModeEffective cho user token: chỉ bật khi config bật VÀ token có claim organizations thật
        boolean orgModeEffective = orgEnabledConfig && jwt.getClaims().containsKey(orgClaimName);

        String tenantId = null;

        tenantId = extractFirstString(jwt.getClaims().get(tenantClaim));

        if (orgModeEffective) {
            // ưu tiên tenantClaim nếu có
            if (tenantId == null || tenantId.isBlank()) {
                tenantId = resolveTenantFromOrganization(jwt, request);
            }
            if (tenantId == null || tenantId.isBlank()) {
                writeUnauthorized(response,
                        "Missing tenant in JWT (claim '" + tenantClaim + "' or organization claim '" + orgClaimName + "')");
                return null;
            }
            return tenantId.trim();
        }

        if (tenantId == null || tenantId.isBlank()) {
            writeUnauthorized(response, "Missing tenant in JWT (claim '" + tenantClaim + "')");
            return null;
        }
        return tenantId.trim();
    }

    private boolean isServiceToServiceToken(Jwt jwt) {
        if (jwt == null) return false;

        // Keycloak thường có gty=client-credentials cho client_credentials flow
        String gty = jwt.getClaimAsString("gty");
        if (gty != null && gty.equalsIgnoreCase("client-credentials")) return true;

        // Service account thường có preferred_username = "service-account-<clientId>"
        String preferred = jwt.getClaimAsString("preferred_username");
        return preferred != null && preferred.startsWith("service-account-");
    }

    private Jwt getJwtFromSecurityContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jat) {
            return jat.getToken();
        }
        return null;
    }

    /**
     * Resolve tenant từ Keycloak Organizations claim.
     * Token dạng:
     * "organization": {
     *   "aliasA": { "tenant_id": ["TENANT_A"] },
     *   "aliasB": { "tenant_id": ["TENANT_B"] }
     * }
     */
    private String resolveTenantFromOrganization(Jwt jwt, HttpServletRequest request) {
        Object orgObj = jwt.getClaims().get(orgClaimName);
        if (!(orgObj instanceof Map<?, ?> orgMap) || orgMap.isEmpty()) return null;

        // chọn org alias
        String chosenAlias;
        if (orgMap.size() == 1) {
            chosenAlias = orgMap.keySet().iterator().next().toString();
        } else {
            String aliasFromHeader = request.getHeader(orgSelectHeader);
            if (aliasFromHeader == null || aliasFromHeader.isBlank()) return null;

            chosenAlias = aliasFromHeader.trim();
            if (!orgMap.containsKey(chosenAlias)) return null;
        }

        // ✅ LƯU ORG ALIAS VÀO CONTEXT
        OrganizationContext.setOrgAlias(chosenAlias);

        Object infoObj = orgMap.get(chosenAlias);
        if (!(infoObj instanceof Map<?, ?> infoMap)) return null;

        Object val = infoMap.get(orgTenantAttribute);
        String mapped = extractFirstString(val);
        if (mapped != null && !mapped.isBlank()) return mapped.trim();

        // fallback: alias (nếu bạn muốn)
        return chosenAlias;
    }

    private String extractFirstString(Object val) {
        if (val == null) return null;
        if (val instanceof String s) return s;
        if (val instanceof Collection<?> c) {
            for (Object x : c) {
                if (x != null) {
                    String s = x.toString();
                    if (!s.isBlank()) return s;
                }
            }
            return null;
        }
        return val.toString();
    }

    private String readTenantFromHeaderOr400(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tenantId = request.getHeader(tenantHeader);
        if (tenantId == null || tenantId.isBlank()) {
            writeBadRequest(response, "Missing tenant header: " + tenantHeader);
            return null;
        }
        return tenantId.trim();
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\":\"unauthorized\",\"message\":\"" + escapeJson(message) + "\"}");
    }

    private void writeBadRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\":\"bad_request\",\"message\":\"" + escapeJson(message) + "\"}");
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private boolean isNotMasterTenant(String tenantId) {
        return tenantId == null || !"master".equalsIgnoreCase(tenantId.trim());
    }

    private boolean isCamundaRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri == null) return false;
        return uri.startsWith("/api/engine-rest/") || uri.startsWith("/engine-rest/");
    }
}
