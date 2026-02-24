package com.ngvgroup.bpm.core.persistence.service.storedprocedure;

import com.ngvgroup.bpm.core.common.spring.SpringContext;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureResponse;
import com.ngvgroup.bpm.core.security.UsernameNormalizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

/**
 * Lớp service cơ sở chứa các phương thức tiện ích chung
 */
public abstract class BaseStoredProcedureService {

    //    @Autowired
    protected StoredProcedureService storedProcedureService;

    private UsernameNormalizer normalizer() {
        UsernameNormalizer n = SpringContext.getOrNull(UsernameNormalizer.class);
        return (n != null) ? n : (u -> u); // fallback an toàn
    }


    protected String getCurrentUserFullName() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Không tìm thấy người dùng đã xác thực");
        }
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaim("given_name") + " " + jwt.getClaim("family_name");
        }

        throw new AccessDeniedException("Loại xác thực không hợp lệ");
    }

    /**
     * Lấy tên đăng nhập của người dùng hiện tại từ JWT token
     *
     * @return tên đăng nhập của người dùng hiện tại
     * @throws AccessDeniedException nếu không tìm thấy người dùng đã xác thực
     */
    protected String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Không tìm thấy người dùng đã xác thực");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {


            String preferred = jwt.getClaimAsString("preferred_username");
            return normalizer().normalize(preferred);

        }

        throw new AccessDeniedException("Loại xác thực không hợp lệ");
    }


    protected String getClientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Không tìm thấy người dùng đã xác thực");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return (String) jwt.getClaim("client_id");
        }

        throw new AccessDeniedException("Loại xác thực không hợp lệ");
    }

    /**
     * Lấy ID của người dùng hiện tại từ JWT token
     *
     * @return ID của người dùng hiện tại
     * @throws AccessDeniedException nếu không tìm thấy người dùng đã xác thực
     */
    protected String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Không tìm thấy người dùng đã xác thực");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        }

        throw new AccessDeniedException("Loại xác thực không hợp lệ");
    }

    /**
     * Kiểm tra xem người dùng hiện tại có phải là người dùng được chỉ định không
     *
     * @param userId ID của người dùng cần kiểm tra
     * @throws AccessDeniedException nếu người dùng hiện tại không phải là người dùng được chỉ định
     */
    protected void checkUser(String userId) {
        String currentUserId = getCurrentUserId();
        if (!currentUserId.equals(userId)) {
            throw new AccessDeniedException("Người dùng không có quyền thực hiện hành động này");
        }
    }

    /**
     * Gọi stored procedure với List parameters
     *
     * @param procedureName tên stored procedure
     * @param parameters    danh sách parameters
     * @return kết quả từ stored procedure
     */
    protected StoredProcedureResponse callStoredProcedure(String procedureName, List<StoredProcedureParameter> parameters) {
        return storedProcedureService.execute(procedureName, parameters);
    }

    /**
     * Gọi stored procedure với retry mechanism
     *
     * @param procedureName tên stored procedure
     * @param parameters    map chứa parameters
     * @param maxRetries    số lần thử lại tối đa
     * @return kết quả từ stored procedure
     */
    protected StoredProcedureResponse callStoredProcedureWithRetry(String procedureName,
                                                                   Map<String, Object> parameters,
                                                                   int maxRetries) {
        return storedProcedureService.executeWithRetry(procedureName, parameters, maxRetries);
    }

    /**
     * Kiểm tra kết nối database
     *
     * @return true nếu kết nối thành công
     */
    protected boolean testDatabaseConnection() {
        return storedProcedureService.testConnection();
    }

    /**
     * Lấy thông tin connection pool
     *
     * @return thông tin connection pool
     */
    protected Map<String, Object> getConnectionPoolInfo() {
        return storedProcedureService.getConnectionPoolInfo();
    }
} 