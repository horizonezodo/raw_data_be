package com.ngv.aia_service.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final int errorCode;
    public BusinessException(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
