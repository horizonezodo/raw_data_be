package ngvgroup.com.rpt.features.aia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ConversationsResponse {
    
    @JsonProperty("error_code")
    private Integer errorCode;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("data")
    private List<ConversationItem> data;
}