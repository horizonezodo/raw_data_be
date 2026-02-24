package com.ngv.zns_service.event.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.event.EventHandler;
import com.ngv.zns_service.service.OAService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Getter
@Component
public class EventUserClickChatNowHandler implements EventHandler {
    
    private final String eventName = "user_click_chatnow";
    private final OAService oaService;
    
    @Override
    public void handleEvent(ObjectNode data) {
        String userId = data.get("user_id").asText();
        String oaId = data.get("oa_id").asText();
        String msgText = "QUAN TÂM TÀI KHOẢN NGV ĐỂ BẮT ĐẦU!";
        String imageUrl = "https://images.spiceworks.com/wp-content/uploads/2022/03/03130947/shutterstock_765149101-1-scaled.jpg";
        List<Map<String, Object>> buttons = new ArrayList<>();
        
        //Set button 1
        Map<String, Object> button1 = new HashMap<>();
        Map<String, Object> payload1 = new HashMap<>();
        payload1.put("url", "https://zalo.me/2247136340485400447");
        button1.put("title", "Quan tâm ngay!");
        button1.put("type", "oa.open.url");
        button1.put("payload", payload1);
        
        //Set button 2
        Map<String, Object> button2 = new HashMap<>();
        Map<String, Object> payload2 = new HashMap<>();
        payload2.put("phone_code", "0879099099");
        button2.put("title", "Gọi đến 0879099099 để được hỗ trợ!");
        button2.put("type", "oa.open.phone");
        button2.put("payload", payload2);
        
        buttons.add(button1);
        buttons.add(button2);
        
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> recipient = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> attachment = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> element = new HashMap<>();
        
        // Set recipient and message content
        recipient.put("user_id", userId);
        message.put("text", msgText);
        
        // Set attachment with media type and URL
        element.put("media_type", "image");
        element.put("url", imageUrl);
        payload.put("template_type", "media");
        payload.put("elements", Arrays.asList(element));
        attachment.put("type", "template");
        attachment.put("payload", payload);
        
        // Add attachment to message
        message.put("attachment", attachment);
        
        // Add recipient and message to body
        body.put("recipient", recipient);
        body.put("message", message);
        
        oaService.sendMessage(oaId, recipient, message);
    }
    
    @Override
    public String getEventName() {
        return eventName;
    }
}
