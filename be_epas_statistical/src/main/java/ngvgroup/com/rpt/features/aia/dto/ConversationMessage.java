package ngvgroup.com.rpt.features.aia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConversationMessage {
    
    @JsonProperty("message_id")
    private String messageId;
    
    @JsonProperty("conversation_id")
    private String conversationId;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("content")
    private String content;
    
    @JsonProperty("created_at")
    private String createdAt;
}