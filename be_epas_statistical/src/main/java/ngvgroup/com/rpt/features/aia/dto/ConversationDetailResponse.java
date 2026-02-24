package ngvgroup.com.rpt.features.aia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConversationDetailResponse {
    
    @JsonProperty("error_code")
    private Integer errorCode;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("data")
    private ConversationDetail data;
}