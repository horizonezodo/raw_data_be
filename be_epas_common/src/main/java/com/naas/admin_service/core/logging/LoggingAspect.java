package com.naas.admin_service.core.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.naas.admin_service.core.contants.Constant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.resteasy.client.jaxrs.internal.proxy.ResteasyClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Aspect for logging execution of service and repository Spring components.
 * <p>
 * By default, it only runs with the "dev" profile.
 */
@Aspect
public class LoggingAspect {

    private static final String RESULT = "result";

    private final Environment env;
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public LoggingAspect(Environment env) {
        this.env = env;
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the
        // advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.naas.admin_service.features..controller..*) " +
          "|| within(com.naas.admin_service.features..service..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the
        // advices.
    }

    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice.
     * @param e         exception.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode logData = objectMapper.createObjectNode();
        logData.put("Class name", joinPoint.getTarget().getClass().getName());
        logData.put("Method name", joinPoint.getSignature().getName());
        logData.put("Argument", Arrays.toString(joinPoint.getArgs()));
        logData.put("Error message", e.getMessage());
        logData.put("Error cause", e.getCause() != null ? e.getCause().toString() : "NULL");
        logData.put("Timestamp", LocalDateTime.now().toString());
        logger(joinPoint)
                .error(
                        "{}",
                        logData);
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        ObjectNode logData = mapper.createObjectNode();
        logData.put("Class name", joinPoint.getTarget().getClass().getName());
        logData.put("Method name", joinPoint.getSignature().getName());
        logData.put("Argument", Arrays.toString(joinPoint.getArgs()));
        if (log.isInfoEnabled()) {
            log.info("Enter: {}", logData);
        }
        logData.put("Timestamp", LocalDateTime.now().toString());
        try {
            Object result = joinPoint.proceed();
            if (result instanceof ResteasyClientProxy) {
                log.warn("Skipping serialization for RESTEasy proxy object: {}", result);
            } else if (result instanceof BufferedImage) {
                log.info(Constant.EXIT_PARAMS_LOG, logData.put(RESULT, "[BufferedImage]"));
            } else if (result instanceof OutputStream) {
                log.info(Constant.EXIT_PARAMS_LOG, logData.put(RESULT, "[OutputStream]"));
            } else if (result instanceof HttpServletResponse) {
                log.info(Constant.EXIT_PARAMS_LOG, logData.put(RESULT, "[HttpServletResponse]"));
            } else {
                if (log.isInfoEnabled()) {
                    logData.set(RESULT, mapper.valueToTree(result));
                    log.info("Exit: {}",
                            logData);
                }
            }

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getName());
            throw e;
        }
    }
}
