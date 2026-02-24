package com.ngv.zns_service.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class WebhookControlller {

    private final WebhookService webhookService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleOAEvent(@RequestHeader("X-ZEvent-Signature") String signature,
                                                @RequestBody ObjectNode payload) {
        webhookService.handleWebhook(signature, payload);
        System.out.println(payload);
        System.out.println(signature);
        return ResponseEntity.ok("Handle event successfully");
    }
}
