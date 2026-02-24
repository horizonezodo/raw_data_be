package com.ngvgroup.bpm.core.logging.audit.annotation;

import java.lang.annotation.*;

/**
 * Gắn lên field entity để KHÔNG audit field đó.
 * Ví dụ: @AuditIgnore cho các cột JSON to, PII, ...
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditIgnore {
}