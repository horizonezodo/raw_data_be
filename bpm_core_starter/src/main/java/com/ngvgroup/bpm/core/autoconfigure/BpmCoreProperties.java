package com.ngvgroup.bpm.core.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "bpm.core")
public class BpmCoreProperties {
    private Cors cors = new Cors();
    private Web web = new Web();
    private Oauth2 oauth2 = new Oauth2();

    /**
     * Các cấu hình liên quan tới security của bpm-core (method security, permission switching...)
     */
    private Security security = new Security();


    @Data
    public static class Cors {
        private List<String> allowedOrigins = List.of();
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
        private List<String> allowedHeaders = List.of("*");
        private List<String> exposedHeaders = List.of("Authorization");
        private boolean allowCredentials = true;
        private long maxAge = 3600L;
    }

    @Data
    public static class Web {
        private List<String> ignoringPaths = List.of();
        private List<String> publicPaths = List.of();
    }

    @Data
    public static class Oauth2 {
        private String tokenUri;
        private String clientId;
        private String clientSecret;
        private boolean cacheToken = true;
    }

    @Data
    public static class Security {
        /**
         * Switch cơ chế phân quyền cho @PreAuthorize("hasRole('X')"):
         * - useDb=false: đọc role từ JWT (Keycloak) như hiện tại
         * - useDb=true : đọc permission_code từ DB/Redis, convert thành ROLE_permission_code
         */
        private Permission permission = new Permission();

        @Data
        public static class Permission {
            /**
             * true  = check quyền theo DB/Redis (COM_CFG_PERMISSION_MAP)
             * false = check quyền theo Keycloak realm_access.roles
             */
            private boolean useDb = false;

            /**
             * TTL cache quyền trong Redis (áp dụng cho key perm:user:* và perm:group:*)
             */
            private Duration cacheTtl = Duration.ofMinutes(30);

            /**
             * Prefix cho Redis key permission.
             * Mặc định: perm:
             */
            private String cacheKeyPrefix = "perm:";
        }
    }

    private boolean activityLogEnabled = true;
} 