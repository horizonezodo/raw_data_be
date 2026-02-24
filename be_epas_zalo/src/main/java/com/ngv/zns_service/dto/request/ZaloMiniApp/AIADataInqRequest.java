package com.ngv.zns_service.dto.request.ZaloMiniApp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class AIADataInqRequest {

    private class ChatMessage {
        private String role;
        private String content;
    }
    private String message;
    private String zaloId;
    private String phoneNumber;
    private String customerId;
    private String sourceId;
    private ChatMessage[] history;

    // Getter & Setter
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getZaloId() { return zaloId; }
    public void setZaloId(String zaloId) { this.zaloId = zaloId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }

    public ChatMessage[] getHistory() { return history; }
    public void setHistory(ChatMessage[] history) { this.history = history; }
}
