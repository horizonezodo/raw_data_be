package com.ngvgroup.bpm.core.common.spring;

import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Giữ ApplicationContext dạng static để các lớp base/abstract (không phải Spring bean)
 * vẫn lấy được bean runtime.
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    public static <T> T getOrNull(Class<T> type) {
        if (ctx == null) return null;
        try {
            return ctx.getBean(type);
        } catch (Exception e) {
            return null;
        }
    }
}
