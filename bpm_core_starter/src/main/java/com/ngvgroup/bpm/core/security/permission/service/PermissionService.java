package com.ngvgroup.bpm.core.security.permission.service;

import java.util.Collection;
import java.util.Set;

public interface PermissionService {
    /**
     * Lấy permission codes cho user hiện tại (đọc từ SecurityContext).
     * - Ở DB-mode: sẽ dùng preferred_username + groups từ JWT.
     * - Ở KC-mode: method này không bắt buộc dùng (vì hasRole lấy từ authorities).
     */
    Set<String> findPermissionCodes();

    /**
     * Lấy permission codes hiệu lực cho 1 user cụ thể (dùng để build GrantedAuthorities).
     *
     * @param userId   preferred_username (theo yêu cầu mới)
     * @param groups   danh sách group name lấy từ JWT claim "groups"
     */
    Set<String> findPermissionCodes(String userId, Collection<String> groups);

    /** Evict cache theo userId (preferred_username). */
    void evictUser(String userId);

    /** Evict cache theo groupName. */
    void evictGroup(String groupName);

    /**
     * Backward-compat (code cũ đang gọi evict(userId)).
     */
    default void evict(String userId) {
        evictUser(userId);
    }
}