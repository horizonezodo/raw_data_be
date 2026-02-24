package com.ngv.aia_service.constant;

import java.text.MessageFormat;

public enum ErrorCode {
    INPUT_INVALID(4001, "Input is invalid | {0}"),
    NOT_FOUND(4000, "Entity data not found!"),
    ERROR_INTERGRATED(4002, "Intergrated error!"),;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int code() {
        return this.code;
    }
    public String message() {
        return this.message;
    }
    public String message(Object... dynamicParts) {
        return MessageFormat.format(this.message, dynamicParts);
    }
}
