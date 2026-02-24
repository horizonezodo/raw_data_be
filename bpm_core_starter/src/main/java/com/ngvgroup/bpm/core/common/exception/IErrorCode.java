package com.ngvgroup.bpm.core.common.exception;

import org.springframework.http.HttpStatus;

public interface IErrorCode {
    Integer getCode();
    String getMessage();
    HttpStatus getHttpStatus();
} 