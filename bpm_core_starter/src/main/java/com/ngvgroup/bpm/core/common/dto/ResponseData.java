package com.ngvgroup.bpm.core.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import com.ngvgroup.bpm.core.web.interceptor.RequestIdInterceptor;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {
    private String requestId;
    private LocalDateTime timestamp;
    private String path;
    private Integer status;
    private String message;
    private T data;
    
    public ResponseData() {
        this.status = ErrorCode.OK.getCode();
        this.message = ErrorCode.OK.getMessage();
        this.timestamp = LocalDateTime.now();
        this.requestId = getCurrentRequestId();
        this.path = getCurrentPath();
    }

    public ResponseData(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.requestId = getCurrentRequestId();
        this.path = getCurrentPath();
    }

    private String getCurrentRequestId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_ATTRIBUTE);
        }
        return null;
    }

    private String getCurrentPath() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getRequestURI();
            }
        } catch (Exception e) {
            log.error("Error getting path", e);
        }
        return null;
    }

    public static <T> ResponseEntity<ResponseData<T>> okEntity(T body) {
        ResponseData<T> response = new ResponseData<>();
        response.setData(body);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ResponseData<Void>> okEntity() {
        ResponseData<Void> response = new ResponseData<>();
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ResponseData<Void>> noContentEntity() {
        ResponseData<Void> response = new ResponseData<>();
        response.setStatus(ErrorCode.NO_CONTENT.getCode());
        response.setMessage(ErrorCode.NO_CONTENT.getMessage());
        return ResponseEntity.status(ErrorCode.NO_CONTENT.getHttpStatus()).body(response);
    }

    public static ResponseEntity<ResponseData<Void>> createdEntity() {
        ResponseData<Void> response = new ResponseData<>();
        response.setStatus(ErrorCode.CREATED.getCode());
        response.setMessage(ErrorCode.CREATED.getMessage());
        return ResponseEntity.status(ErrorCode.CREATED.getHttpStatus()).body(response);
    }

    public static <T> ResponseEntity<ResponseData<T>> errorEntity(Integer status, String message, HttpStatus httpStatus) {
        ResponseData<T> response = new ResponseData<>(status, message, null);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
