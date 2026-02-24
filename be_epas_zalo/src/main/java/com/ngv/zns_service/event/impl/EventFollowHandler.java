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
@Component
@Getter
public class EventFollowHandler implements EventHandler {
    
    private final String eventName = "follow";
    private final OAService oaService;
    
    @Override
    public void handleEvent(ObjectNode data) {
        String userId = data.get("follower").get("id").asText();
        String oaId = data.get("oa_id").asText();
        String msgText = "Dạ em chào Anh/Chị, em có thể hỗ trợ gì cho mình ạ?";
        Map<String, Object> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        
        recipient.put("user_id", userId);
        message.put("text", msgText);
        oaService.sendMessage(oaId, recipient, message);
    }
    
    @Override
    public String getEventName() {
        return eventName;
    }
}
