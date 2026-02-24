package com.ngvgroup.bpm.core.persistence.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ✅ Multi-tenant properties
 * Lib dùng để:
 * - Lấy tenantId từ JWT claim (tenantClaim) hoặc map từ issuer (registry)
 * - Public API (permitAll) cần lấy tenantId từ HEADER
 * - Route DataSource theo tenant

 * ✅ BỔ SUNG THEO RULE MỚI:
 * - schemaPattern để tạo schema = {tenantId}_{serviceCode}
 * - resolveSchema(tenantId, serviceCode)

 * ✅ HỖ TRỢ KEYCLOAK ORGANIZATIONS:
 * - Nếu bật organizations.enabled=true:
 *   + App sẽ resolve tenant từ claim "organization" trong JWT (Keycloak Organizations)
 *   + Ưu tiên attribute (vd: organization.<alias>.tenant_id) để map về TENANT_DB_CONFIG.TENANT_ID
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "multitenancy")
public class MultitenancyProperties {

    private boolean enabled = false;

    /** Claim tenant_id trong JWT (default: tenant_id). Hữu ích cho S2S (client_credentials) */
    private String tenantClaim = "tenant_id";

    /** Header name dùng cho PUBLIC API (khi không có token). Default: X-Tenant-Id */
    private String tenantHeader = "X-Tenant-Id";

    /**
     * ✅ Pattern tạo schema.
     * Default: "{tenantId}_{serviceCode}"
     *
     * Ví dụ:
     * - tenantId = tenantA
     * - serviceCode = bpm-core
     * => schema raw: tenantA_bpm-core (sau đó sẽ sanitize ở DataSourceCache)
     */
    private String schemaPattern = "{tenantId}_{serviceCode}";

    /** ✅ Keycloak Organizations mode */
    private Organizations organizations = new Organizations();

    private Pool pool = new Pool();
    private Refresh refresh = new Refresh();

    /** ✅ Normalize tenantClaim: tránh cấu hình rỗng/space gây lỗi */
    public void setTenantClaim(String tenantClaim) {
        this.tenantClaim = (tenantClaim == null || tenantClaim.isBlank())
                ? "tenant_id"
                : tenantClaim.trim();
    }

    /** ✅ Normalize tenantHeader: tránh cấu hình rỗng/space gây lỗi */
    public void setTenantHeader(String tenantHeader) {
        this.tenantHeader = (tenantHeader == null || tenantHeader.isBlank())
                ? "X-Tenant-Id"
                : tenantHeader.trim();
    }

    /** ✅ Normalize schemaPattern */
    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = (schemaPattern == null || schemaPattern.isBlank())
                ? "{tenantId}_{serviceCode}"
                : schemaPattern.trim();
    }

    /**
     * ✅ Resolve schema name theo tenantId + serviceCode
     * - Chỉ build chuỗi theo pattern
     * - Sanitize để hợp lệ DB sẽ làm ở DataSourceCache (để dễ debug raw value)
     */
    public String resolveSchema(String tenantId, String serviceCode) {
        if (tenantId == null || tenantId.isBlank()) return null;

        String t = tenantId.trim();
        String s = (serviceCode == null || serviceCode.isBlank()) ? "" : serviceCode.trim();

        String p = (schemaPattern == null || schemaPattern.isBlank())
                ? "{tenantId}_{serviceCode}"
                : schemaPattern.trim();

        return p.replace("{tenantId}", t)
                .replace("{serviceCode}", s);
    }

    @Getter
    @Setter
    public static class Organizations {
        /** Bật chế độ Keycloak Organizations để resolve tenant từ claim organization */
        private boolean enabled = false;

        /** Tên claim chứa organizations trong JWT (mặc định: organization) */
        private String claim = "organization";

        /**
         * Tên attribute trong organization để map về tenantId nội bộ.
         * Ví dụ bạn set trong Keycloak Organization Attributes: tenant_id=EPAS_DEMO
         */
        private String tenantAttribute = "tenant_id";

        /** Ưu tiên lấy tenantId từ tenantAttribute. Nếu không có, fallback alias org */
        private boolean preferTenantAttribute = true;

        /** Nếu user thuộc nhiều org: chọn org alias bằng header này */
        private String selectHeader = "X-Org-Alias";

        public void setClaim(String claim) {
            this.claim = (claim == null || claim.isBlank()) ? "organization" : claim.trim();
        }

        public void setTenantAttribute(String tenantAttribute) {
            this.tenantAttribute = (tenantAttribute == null || tenantAttribute.isBlank()) ? "tenant_id" : tenantAttribute.trim();
        }

        public void setSelectHeader(String selectHeader) {
            this.selectHeader = (selectHeader == null || selectHeader.isBlank()) ? "X-Org-Alias" : selectHeader.trim();
        }
    }

    @Getter
    @Setter
    public static class Pool {
        private int maxSize = 5;
        private int minIdle = 0;
        private long idleTimeoutMs = 1_800_000;
        private long maxLifetimeMs = 1_800_000;
        private long connectionTimeoutMs = 30_000;
        private long cacheMaxSize = 500;
    }

    @Getter
    @Setter
    public static class Refresh {
        private boolean enabled = true;
        private long intervalMs = 300_000;
    }
}
