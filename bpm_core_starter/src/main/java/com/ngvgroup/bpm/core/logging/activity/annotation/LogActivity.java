package com.ngvgroup.bpm.core.logging.activity.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface LogActivity {

    /**
     * Có log hay không
     */
    boolean enable() default true;

    /**
     * Mô tả hành động (VD: "Tìm kiếm khách hàng")
     * Nếu không set, sẽ lấy theo HTTP method
     */
    String action() default "";
    String function() default "";
}