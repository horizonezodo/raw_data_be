package ngvgroup.com.rpt.features.integration;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

public class OptionConst {
    
    // AIA Gateway related constants
    public static final String AIA_GATEWAY_URL_GET_TOKEN = "AIA_AUTH_LOGIN_URL"; // URL dạng: localhost:8000/api/auth/login
    public static final String AIA_GATEWAY_AUTHENTICATION_USER = "AIA_USER";
    public static final String AIA_GATEWAY_AUTHENTICATION_PASS = "AIA_PASSWORD";
    
    // AIA specific constants
    public static final String AIA_GATEWAY_CHAT_MESSAGE = "AIA_CHAT_MESSAGE"; // URL dạng: localhost:8000/api/chat/message
    public static final String AIA_GATEWAY_CONVERSATIONS = "AIA_CONVERSATIONS"; // URL dạng: http://113.190.234.241:9798/api/conversations
    public static final String AIA_GATEWAY_CONVERSATION_DETAIL = "AIA_CONVERSATION_DETAIL"; // URL dạng: localhost:8000/api/conversations
    
    // Private constructor to prevent instantiation
    private OptionConst() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}