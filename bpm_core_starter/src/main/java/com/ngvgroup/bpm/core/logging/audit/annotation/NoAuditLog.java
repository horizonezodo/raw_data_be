package com.ngvgroup.bpm.core.logging.audit.annotation;

import java.lang.annotation.*;

/**
 * Gắn vào Controller hoặc method để:
 *  - VẪN log activity
 *  - KHÔNG log audit (Hibernate listener bỏ qua request này)
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuditLog {
}
