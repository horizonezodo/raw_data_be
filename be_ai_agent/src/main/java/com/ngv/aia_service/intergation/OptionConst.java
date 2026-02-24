package com.ngv.aia_service.intergation;

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

    // AIA Efund constants
    public static final String EFUND_GATEWAY_AIA_DATA_INQ = "AIA_DATA_INQ";
    
    // Private constructor to prevent instantiation
    private OptionConst() {
        throw new IllegalStateException("Utility class");
    }
}