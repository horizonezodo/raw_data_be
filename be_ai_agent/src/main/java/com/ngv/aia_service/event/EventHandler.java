package com.ngv.aia_service.event;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface EventHandler {
    void handleEvent(ObjectNode data);
    String getEventName();
}
