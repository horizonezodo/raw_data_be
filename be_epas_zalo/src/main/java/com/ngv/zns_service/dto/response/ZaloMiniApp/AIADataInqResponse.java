package com.ngv.zns_service.dto.response.ZaloMiniApp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIADataInqResponse {
    private class ChatMessage {
        private String role;
        private String content;
    }
    private String answer;
    private ChatMessage[] history;
}
