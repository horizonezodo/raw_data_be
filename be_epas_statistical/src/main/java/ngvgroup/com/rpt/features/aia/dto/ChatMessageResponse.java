package ngvgroup.com.rpt.features.aia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    
    @JsonProperty("error_code")
    private Integer errorCode;
    
    private String message;
    
    private ChatData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatData {
        @JsonProperty("conversation_id")
        private String conversationId;
        
        private String response;
        
        @JsonProperty("created_at")
        private String createdAt;
    }
}