package com.ngvgroup.bpm.core.logging.activity.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD , ElementType.TYPE})
@Documented
public @interface NoActivityLog {
}