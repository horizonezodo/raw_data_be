package com.ngvgroup.bpm.core.logging.audit.service;

import com.ngvgroup.bpm.core.logging.activity.toggle.LoggingToggleProvider;
import com.ngvgroup.bpm.core.logging.activity.annotation.NoActivityLog;
import com.ngvgroup.bpm.core.logging.audit.annotation.NoAuditLog;
import com.ngvgroup.bpm.core.logging.audit.domain.EntityChange;
import com.ngvgroup.bpm.core.web.interceptor.RequestIdInterceptor;
import com.ngvgroup.bpm.core.logging.audit.dto.AuditFieldChange;
import com.ngvgroup.bpm.core.logging.audit.dto.AuditLogMessage;
import com.ngvgroup.bpm.core.logging.kafka.service.LoggingKafkaProducerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public record EntityChangeService(
        LoggingToggleProvider toggleProvider,
        LoggingKafkaProducerService kafkaProducerService
) implements EntityChangeHandler {


    @Override
    public void handle(EntityChange change) {

        log.debug("[AUDIT] handle() changeType={}, table={}, id={}, fields={}",
                change.getChangeType(), change.getTableName(), change.getEntityId(),
                change.getFieldChanges() != null ? change.getFieldChanges().size() : 0);

        // 1) Global OFF
        if (!toggleProvider.isAuditLogEnabled()) {
            return;
        }

        // 2) Bỏ qua bảng log
        if (change.getTableName() != null &&
                change.getTableName().toUpperCase().startsWith("COM_INF_LOG_")) {
            return;
        }

        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs != null ? attrs.getRequest() : null;

        // Ở đây kiểm tra trực tiếp annotation @NoAuditLog / @NoActivityLog
        if (request != null && shouldSkipAuditByAnnotation(request)) {
            log.debug("[AUDIT] Skip audit by @NoAuditLog/@NoActivityLog, table={}, id={}",
                    change.getTableName(), change.getEntityId());
            return;
        }

        List<AuditFieldChange> changes = change.getFieldChanges();
        if (changes == null || changes.isEmpty()) {
            return;
        }

        String username = resolveUsername();
        String clientIp = request != null ? request.getRemoteAddr() : null;
        String browserInf = request != null ? request.getHeader("User-Agent") : null;
        String requestId = resolveRequestId(request);

        AuditLogMessage msg = AuditLogMessage.builder()
                .requestId(requestId)
                .eventTime(new Date())
                .tableName(change.getTableName())
                .entityName(change.getEntityName())
                .recordId(change.getEntityId() != null ? String.valueOf(change.getEntityId()) : null)
                .changeType(change.getChangeType())
                .username(username)
                .clientIp(clientIp)
                .browserInfo(browserInf)
                .changes(changes)
                .build();

        kafkaProducerService.sendAuditLog(msg);
    }

    /**
     * Tự lấy HandlerMethod hiện tại và đọc annotation.
     */
    private boolean shouldSkipAuditByAnnotation(HttpServletRequest request) {
        Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            // Không phải request qua controller (VD: job, batch) → vẫn audit
            return false;
        }

        Class<?> controllerClass = handlerMethod.getBeanType();

        // Nếu method hoặc class có @NoAuditLog → bỏ qua audit
        if (handlerMethod.hasMethodAnnotation(NoAuditLog.class)
                || controllerClass.isAnnotationPresent(NoAuditLog.class)) {
            return true;
        }

        // Nếu method/class có @NoActivityLog mà bạn muốn "tắt luôn cả audit"
        return handlerMethod.hasMethodAnnotation(NoActivityLog.class)
                || controllerClass.isAnnotationPresent(NoActivityLog.class);
    }

    private String resolveUsername() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return "ANONYMOUS";
            }
            Object principal = auth.getPrincipal();
            if (principal instanceof String s) {
                return s;
            }
            return auth.getName();
        } catch (Exception e) {
            log.warn("Cannot resolve username from SecurityContext: {}", e.getMessage());
            return "ANONYMOUS";
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        if (request == null) {
            return UUID.randomUUID().toString();
        }

        Object attr = request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        if (attr instanceof String s && !s.isBlank()) {
            return s;
        }

        String header = request.getHeader(RequestIdInterceptor.REQUEST_ID_HEADER);
        if (header != null && !header.isBlank()) {
            return header;
        }

        return UUID.randomUUID().toString();
    }
}
