package com.ngvgroup.bpm.core.security.permission.service.impl;

import com.ngvgroup.bpm.core.security.permission.service.PermissionService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;

/**
 * RedisPermissionService (REDIS-ONLY)
 *
 * - Lib/core CHỈ đọc Redis snapshot.
 * - Tuyệt đối KHÔNG query DB trong lib.
 * - Redis miss => empty => deny.
 *
 * Key:
 * - {prefix}user:{sub}
 * - {prefix}group:{groupName}
 */
public record RedisPermissionService(StringRedisTemplate redis, String keyPrefix) implements PermissionService {

    private static final String EMPTY_MARKER = "__NONE__";

    @Override
    public Set<String> findPermissionCodes() {
        Jwt jwt = currentJwt();
        if (jwt == null) return Set.of();

        // ✅ userId = sub
        String userId = claimSub(jwt);
        if (userId == null || userId.isBlank()) return Set.of();

        List<String> groups = claimGroups(jwt);
        return findPermissionCodes(userId, groups);
    }

    @Override
    public Set<String> findPermissionCodes(String userId, Collection<String> groups) {
        if (userId == null || userId.isBlank()) return Set.of();

        Set<String> effective = new HashSet<>();

        // 1) user permissions
        effective.addAll(readSet(userKey(userId)));

        // 2) group permissions
        if (groups != null) {
            for (String g : groups) {
                if (g == null || g.isBlank()) continue;
                effective.addAll(readSet(groupKey(g)));
            }
        }

        return effective;
    }

    @Override
    public void evictUser(String userId) {
        if (userId == null || userId.isBlank()) return;
        redis.delete(userKey(userId));
    }

    @Override
    public void evictGroup(String groupName) {
        if (groupName == null || groupName.isBlank()) return;
        redis.delete(groupKey(groupName));
    }

    // =========================
    // Redis helpers
    // =========================

    private String userKey(String userId) {
        return keyPrefix + "user:" + userId;
    }

    private String groupKey(String groupName) {
        return keyPrefix + "group:" + groupName;
    }

    /**
     * Redis-only:
     * - key không tồn tại => empty
     * - key có marker __NONE__ => empty
     */
    private Set<String> readSet(String key) {
        Boolean exists = redis.hasKey(key);
        if (exists == null || !exists) return Set.of(); // ✅ null-safe

        Set<String> members = redis.opsForSet().members(key);
        if (members == null || members.isEmpty()) return Set.of();
        if (members.contains(EMPTY_MARKER)) return Set.of();

        return members;
    }

    // =========================
    // JWT helpers
    // =========================

    private Jwt currentJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }
        return null;
    }

    private static String claimSub(Jwt jwt) {
        try {
            String v = jwt.getClaimAsString("sub");
            if (v != null) return v;
        } catch (Exception ignored) { }
        Object raw = jwt.getClaims().get("sub");
        return raw != null ? raw.toString() : null;
    }

    private static List<String> claimGroups(Jwt jwt) {
        try {
            List<String> v = jwt.getClaimAsStringList("groups");
            return v != null ? v : List.of();
        } catch (Exception e) {
            Object raw = jwt.getClaims().get("groups");
            if (raw instanceof Collection<?> c) {
                List<String> list = new ArrayList<>();
                for (Object o : c) if (o != null) list.add(o.toString());
                return list;
            }
            return List.of();
        }
    }
}
