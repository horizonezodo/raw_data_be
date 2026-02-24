package com.ngvgroup.bpm.core.logging.activity.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.logging.activity.toggle.LoggingToggleProvider;
import com.ngvgroup.bpm.core.logging.activity.annotation.NoActivityLog;
import com.ngvgroup.bpm.core.web.interceptor.RequestIdInterceptor;
import com.ngvgroup.bpm.core.logging.activity.dto.ActivityLogMessage;
import com.ngvgroup.bpm.core.logging.kafka.service.LoggingKafkaProducerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Slf4j
public record ActivityLogAspect(
        LoggingKafkaProducerService kafkaProducerService,
        LoggingToggleProvider toggleProvider,
        String applicationName
) {

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object aroundRestController(ProceedingJoinPoint joinPoint) throws Throwable {

        // Nếu common báo tắt activity => không gửi Kafka luôn
        if (!toggleProvider.isActivityLogEnabled()) {
            return joinPoint.proceed();
        }

        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attrs.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        // Nếu class/method có @NoActivityLog → set cờ skip & bỏ qua luôn (không activity, không audit)
        if (targetClass.isAnnotationPresent(NoActivityLog.class)
                || method.isAnnotationPresent(NoActivityLog.class)) {
            return joinPoint.proceed();
        }

        // Lấy @LogActivity ở method hoặc class
        LogActivity methodAnn = method.getAnnotation(LogActivity.class);
        LogActivity classAnn = targetClass.getAnnotation(LogActivity.class);

        long start = System.currentTimeMillis();
        String requestId = resolveRequestId(request);
        Date eventTime = new Date();
        String statusCode = "200";

        Object result;

        try {
            // ✔ Aspect đang chạy – log thử để bạn check trên console
            log.info("[ActivityLog] Intercept {}.{}", targetClass.getSimpleName(), method.getName());
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            statusCode = "500";
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;

            String username = resolveUsername();
            String clientIp = request.getRemoteAddr();
            String clientName = request.getRemoteHost();
            String userAgent = request.getHeader("User-Agent");

            String url = request.getRequestURI();
            String httpMethod = request.getMethod();

            String action;
            if (methodAnn != null && methodAnn.action() != null && !methodAnn.action().isEmpty()) {
                action = methodAnn.action();
            } else {
                action = buildDefaultAction(httpMethod);
            }

            String functionCode;
            if (classAnn != null && classAnn.function() != null && !classAnn.function().isEmpty()) {
                functionCode = classAnn.function();
            } else {
                functionCode = "";
            }

            String requestPayload = buildDetailJson(joinPoint, request);

            ActivityLogMessage msg = ActivityLogMessage.builder()
                    .requestId(requestId)
                    .eventTime(eventTime)
                    .statusCode(statusCode)
                    .username(username)
                    .functionCode(functionCode)
                    .actionName(action)
                    .requestUrl(url)
                    .methodCode(httpMethod)
                    .serviceName(applicationName)
                    .clientIp(clientIp)
                    .clientName(clientName)
                    .browserInfo(userAgent)
                    .durationTime(duration)
                    .requestPayload(requestPayload)
                    .build();

            kafkaProducerService.sendActivityLog(msg);
        }

        return result;
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

    private String buildDefaultAction(String httpMethod) {
        if ("GET".equalsIgnoreCase(httpMethod)) {
            return "VIEW";
        } else if ("POST".equalsIgnoreCase(httpMethod)) {
            return "CREATE";
        } else if ("PUT".equalsIgnoreCase(httpMethod) || "PATCH".equalsIgnoreCase(httpMethod)) {
            return "UPDATE";
        } else if ("DELETE".equalsIgnoreCase(httpMethod)) {
            return "DELETE";
        }
        return "OTHER";
    }

    private String buildDetailJson(ProceedingJoinPoint jp, HttpServletRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 1) Lấy query parameter
            Map<String, Object> queryParams = new LinkedHashMap<>();
            Map<String, String[]> paramMap = request.getParameterMap();
            paramMap.forEach((k, v) -> {
                if (v == null) {
                    queryParams.put(k, null);
                } else if (v.length == 1) {
                    queryParams.put(k, v[0]);
                } else {
                    queryParams.put(k, v);
                }
            });

            // 2) Lấy args trong method (path variable, body DTO, ...).
            MethodSignature sig = (MethodSignature) jp.getSignature();
            String[] paramNames = sig.getParameterNames();
            Object[] args = jp.getArgs();

            List<Map<String, Object>> argList = new ArrayList<>();
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];

                    // Bỏ qua mấy loại không cần ghi log
                    if (arg instanceof HttpServletRequest) continue;
                    if (arg instanceof jakarta.servlet.http.HttpServletResponse) continue;
                    if (arg instanceof BindingResult) continue;

                    Map<String, Object> entry = new LinkedHashMap<>();
                    String name = (paramNames != null && paramNames.length > i)
                            ? paramNames[i]
                            : "arg" + i;

                    entry.put("name", name);
                    entry.put("value", arg);  // Jackson sẽ serialize object này

                    argList.add(entry);
                }
            }

            Map<String, Object> root = new LinkedHashMap<>();
            root.put("query", queryParams);
            root.put("args", argList);

            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            log.warn("Cannot build request payload", e);
            return "{}";
        }
    }
}
