package ngvgroup.com.bpm.client.utils;

import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;


public class BpmClientSecurityUtils {

    private BpmClientSecurityUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;
                String userName = jwt.getClaimAsString("preferred_username");
                String tenantId = TenantContext.getTenantId();
                return stripTenantSuffix(userName, tenantId);
            } else {
                throw new AccessDeniedException("Loại xác thực không hợp lệ");
            }
        } else {
            throw new AccessDeniedException("Không tìm thấy người dùng đã xác thực");
        }
    }

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;
                String userName = jwt.getClaimAsString("sub");
                String tenantId = TenantContext.getTenantId();
                return stripTenantSuffix(userName, tenantId);
            } else {
                throw new AccessDeniedException("Loại xác thực không hợp lệ");
            }
        } else {
            throw new AccessDeniedException("Không tìm thấy người dùng đã xác thực");
        }
    }

    private static String stripTenantSuffix(String username, String tenantId) {
        if (username == null) {
            return null;
        } else {
            if (tenantId != null && !tenantId.isBlank()) {
                String suffix = "." + tenantId.toLowerCase();
                if (username.endsWith(suffix)) {
                    return username.substring(0, username.length() - suffix.length());
                }
            }

            return username;
        }
    }
}
