package com.naas.admin_service.features.permission.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionCacheWriter {

    private final StringRedisTemplate redis;

    /**
     * Ghi snapshot permission cho USER.
     * Bạn quản lý theo userId = sub.
     * Key: perm:user:{sub}
     */
    public void putUser(String userId, Set<String> perms, Duration ttl) {
        String key = "perm:user:" + userId;

        redis.delete(key);

        if (perms != null && !perms.isEmpty()) {
            redis.opsForSet().add(key, perms.toArray(new String[0]));
        } else {
            redis.opsForSet().add(key, "__NONE__");
        }

        if (ttl != null && !ttl.isZero() && !ttl.isNegative()) {
            redis.expire(key, ttl);
        }
    }

    /**
     * Ghi snapshot permission cho GROUP.
     * Key: perm:group:{groupName}
     */
    public void putGroup(String groupName, Set<String> perms, Duration ttl) {
        String key = "perm:group:" + groupName;

        redis.delete(key);

        if (perms != null && !perms.isEmpty()) {
            redis.opsForSet().add(key, perms.toArray(new String[0]));
        } else {
            redis.opsForSet().add(key, "__NONE__");
        }

        if (ttl != null && !ttl.isZero() && !ttl.isNegative()) {
            redis.expire(key, ttl);
        }
    }

    public void evictUser(String userId) {
        redis.delete("perm:user:" + userId);
    }

    public void evictGroup(String groupName) {
        redis.delete("perm:group:" + groupName);
    }
}