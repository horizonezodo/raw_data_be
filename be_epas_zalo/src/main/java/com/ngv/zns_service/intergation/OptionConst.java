package com.ngv.zns_service.intergation;

public class OptionConst {
    
    // Efund Gateway related constants
    public static final String EFUND_GATEWAY_URL_GET_TOKEN = "URL_GET_TOKEN";
    public static final String EFUND_GATEWAY_AUTHENTICATION_USER = "USER";
    public static final String EFUND_GATEWAY_AUTHENTICATION_PASS = "PASSWORD";
    
    // Base URL constantsBASE_URL
    public static final String EFUND_GATEWAY_BASE_URL = "GW_URL";
    public static final String EFUND_GATEWAY_API_VERSION = "API_VERSION";
    
    // Timeout and retry constants
    public static final String EFUND_GATEWAY_TIMEOUT = "TIMEOUT";
    public static final String EFUND_GATEWAY_MAX_RETRY = "MAX_RETRY";
    
    // Security constants
    public static final String EFUND_GATEWAY_ENCRYPTION_KEY = "ENCRYPTION_KEY";
    public static final String EFUND_GATEWAY_USE_HTTPS = "USE_HTTPS";
    
    //ZMA Efund constants
    public static final String EFUND_GATEWAY_ZMA_DATA_INQ = "ZMA_DATA_INQ";
    public static final String EFUND_GATEWAY_ZMA_TRANSACTION_REGISTRATION = "ZMA_TRANSACTION_REGISTRATION";
    public static final String EFUND_GATEWAY_ZMA_LOGIN = "ZMA_LOGIN";
    public static final String EFUND_GATEWAY_ZMA_SIGNUP = "ZMA_SIGNUP";
    public static final String EFUND_GATEWAY_ZMA_PUBLIC_INFO = "ZMA_PUBLIC_INFO";
    public static final String EFUND_GATEWAY_ZMA_CREATE_TRAN_CORE = "ZMA_CREATE_TRAN_CORE";
    // AIA Efund constants
    public static final String EFUND_GATEWAY_AIA_DATA_INQ = "AIA_DATA_INQ";
    
    // Image streaming constants
    public static final String EFUND_GATEWAY_IMAGE_BASE_URL = "IMAGE_BASE_URL";
    
    // Private constructor to prevent instantiation
    private OptionConst() {
        throw new IllegalStateException("Utility class");
    }
}