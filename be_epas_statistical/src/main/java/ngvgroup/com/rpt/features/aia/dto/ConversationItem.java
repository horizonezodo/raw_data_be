package ngvgroup.com.rpt.features.aia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConversationItem {
    
    @JsonProperty("conversation_id")
    private String conversationId;
    
    @JsonProperty("client_id")
    private String clientId;
    
    @JsonProperty("user_id")
    private String userId;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("created_at")
    private String createdAt;
}