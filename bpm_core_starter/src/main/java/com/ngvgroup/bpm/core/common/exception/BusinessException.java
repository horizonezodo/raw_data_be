package com.ngvgroup.bpm.core.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(
            errorCode.getMessage().contains("%s")
                ? String.format(errorCode.getMessage(), args)
                : (args != null && args.length > 0
                    ? errorCode.getMessage() + " " + args[0]
                    : errorCode.getMessage())
        );
        this.errorCode = errorCode.getCode();
    }
}