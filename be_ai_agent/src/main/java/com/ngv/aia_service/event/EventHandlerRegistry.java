package com.ngv.aia_service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventHandlerRegistry {
    
    private final Map<String, EventHandler> handlerMap = new HashMap<String, EventHandler>();
    
    @Autowired
    public EventHandlerRegistry(List<EventHandler> handlers) {
        handlers.forEach(handler -> {handlerMap.put(handler.getEventName(), handler);});
    }
    
    public EventHandler getEventHandler(String eventName) {
        return handlerMap.get(eventName);
    }
}
