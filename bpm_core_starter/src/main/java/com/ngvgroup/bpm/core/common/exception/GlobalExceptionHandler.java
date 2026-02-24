package com.ngvgroup.bpm.core.common.exception;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.interceptor.RequestIdInterceptor;
import com.ngvgroup.bpm.core.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final int MAX_MESSAGE_LENGTH = 500;

    private ResponseEntity<ResponseData<Void>> validateResponse(Integer status, String message, HttpStatus httpStatus) {
        if (status == null) {
            status = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        }
        if (message == null) {
            message = ErrorCode.INTERNAL_SERVER_ERROR.getMessage();
        }
        if (httpStatus == null) {
            httpStatus = ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus();
        }
        message = StringUtils.truncateWithEllipsis(message, MAX_MESSAGE_LENGTH);
        return ResponseData.errorEntity(status, message, httpStatus);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseData<Void>> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        log.error("[RequestId: {}] Business Exception: {}", requestId, ex.getMessage(), ex);
        return validateResponse(ErrorCode.BAD_REQUEST.getCode(), ex.getMessage(), ErrorCode.BAD_REQUEST.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseData<Void>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        log.error("[RequestId: {}] Access Denied: {}", requestId, ex.getMessage(), ex);
        return validateResponse(ErrorCode.FORBIDDEN.getCode(), ErrorCode.FORBIDDEN.getMessage(), ErrorCode.FORBIDDEN.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData<Void>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("[RequestId: {}] Validation Error: {}", requestId, message);
        return validateResponse(ErrorCode.BAD_REQUEST.getCode(), message, ErrorCode.BAD_REQUEST.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseData<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        log.error("[RequestId: {}] Invalid request body format: {}", requestId, ex.getMessage());
        return validateResponse(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMessage(), ErrorCode.BAD_REQUEST.getHttpStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseData<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        log.error("[RequestId: {}] Method not supported: {}", requestId, ex.getMessage());
        return validateResponse(ErrorCode.METHOD_NOT_ALLOWED.getCode(), ErrorCode.METHOD_NOT_ALLOWED.getMessage(), ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseData<Void>> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        log.error("[RequestId: {}] Endpoint not found: {} {}", requestId, ex.getHttpMethod(), ex.getRequestURL());
        return validateResponse(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<Void>> handleGenericException(Exception ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        log.error("[RequestId: {}] Unexpected error: {}", requestId, ex.getMessage(), ex);
        return validateResponse(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
} 