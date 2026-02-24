package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.security.permission.service.PermissionService;
import com.ngvgroup.bpm.core.security.permission.service.impl.RedisPermissionService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

@EnableAspectJAutoProxy(proxyTargetClass = true) // có thể bỏ luôn nếu không còn aspect nào khác cần
@AutoConfiguration(after = RedisAutoConfiguration.class)
public class BpmPermissionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PermissionService.class)
    @ConditionalOnBean(RedisConnectionFactory.class)
    public PermissionService redisPermissionService(
            ObjectProvider<StringRedisTemplate> redisTemplateProvider,
            RedisConnectionFactory cf,
            BpmCoreProperties bpmCoreProperties
    ) {
        BpmCoreProperties.Security.Permission permProps = resolvePermProps(bpmCoreProperties);

        StringRedisTemplate redis = redisTemplateProvider.getIfAvailable();
        if (redis == null) {
            redis = new StringRedisTemplate(cf);
        }

        return new RedisPermissionService(redis, permProps.getCacheKeyPrefix());
    }

    @Bean
    @ConditionalOnMissingBean({PermissionService.class, RedisConnectionFactory.class})
    public PermissionService denyAllPermissionService() {
        return new PermissionService() {
            @Override
            public Set<String> findPermissionCodes() {
                return Set.of();
            }

            @Override
            public Set<String> findPermissionCodes(String userId, java.util.Collection<String> groups) {
                return Set.of();
            }

            @Override
            public void evictUser(String userId) { /* no-op */ }

            @Override
            public void evictGroup(String groupName) { /* no-op */ }
        };
    }

    private BpmCoreProperties.Security.Permission resolvePermProps(BpmCoreProperties bpmCoreProperties) {
        BpmCoreProperties.Security security = bpmCoreProperties.getSecurity();
        if (security != null && security.getPermission() != null) {
            return security.getPermission();
        }
        return new BpmCoreProperties.Security.Permission();
    }
}
