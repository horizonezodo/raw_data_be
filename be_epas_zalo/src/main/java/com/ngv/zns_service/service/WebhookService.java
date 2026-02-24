package com.ngv.zns_service.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface WebhookService {
    void handleWebhook(String signature, ObjectNode payload);
}
