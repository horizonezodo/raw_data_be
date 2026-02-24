package ngvgroup.com.bpm.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AuditField {
    /**
     * Tên định danh của trường trong bảng Audit (fieldCode).
     * Ví dụ: @AuditField("customer_name")
     */
    String value();
}
