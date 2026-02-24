package com.ngv.zns_service.event.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.event.EventHandler;
import com.ngv.zns_service.service.OAService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Component
public class EventUserSendTextHandler implements EventHandler {
    
    private final String eventName = "user_send_text";
    private final OAService oaService;
    
    @Override
    public void handleEvent(ObjectNode data) {
        String userId = data.get("sender").get("id").asText();
        String oaId = data.get("recipient").get("id").asText();
        Map<String, Object> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        
        recipient.put("user_id", userId);
        String senderMessage = data.get("message").get("text").asText();
        switch (senderMessage) {
            case "#statistic": {
                String msgText = "Ngv xin chào!";
                message.put("text", msgText);
                oaService.sendMessage(oaId, recipient, message);
            }
        }
    }
}
