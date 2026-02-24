package com.ngv.zns_service.constant;

import org.springframework.http.HttpStatus;

import com.ngvgroup.bpm.core.common.exception.ErrorCode;

public class ZaloErrorCode {
    // INPUT_INVALID(4001, "Input is invalid | {0}"),
    // NOT_FOUND(4000, "Entity data not found!"),
    // ERROR_INTERGRATED(4002, "Intergrated error!"),;
    public static ErrorCode INPUT_INVALID = new ErrorCode(4001, "Input is invalid | %s", HttpStatus.BAD_REQUEST) {
        
    };
    public static ErrorCode NOT_FOUND = new ErrorCode(4000, "Entity data not found!", HttpStatus.BAD_REQUEST) {
        
    };    
    public static ErrorCode ERROR_INTERGRATED = new ErrorCode(4002, "Intergrated error!", HttpStatus.BAD_REQUEST) {
        
    };
}
