package com.ngv.zns_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.event.EventHandler;
import com.ngv.zns_service.event.EventHandlerRegistry;
import com.ngv.zns_service.model.entity.ZssApp;
import com.ngv.zns_service.service.WebhookService;
import com.ngv.zns_service.service.ZSSAppService;
import com.ngv.zns_service.util.json.JacksonUtil;
import com.ngv.zns_service.util.string.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class WebhookServiceImpl implements WebhookService {

    private final ZSSAppService zssAppService;
    private final EventHandlerRegistry eventHandlerRegistry;

    @Async("taskExecutor")
    @Override
    public void handleWebhook(String signature, ObjectNode payload) {
        if (!validateSignature(signature, payload) || Objects.isNull(payload)) {
            return;
        }
        String eventName = payload.get("event_name").asText();
        EventHandler eventHandler = eventHandlerRegistry.getEventHandler(eventName);
        if (!Objects.isNull(eventHandler)) {
            eventHandler.handleEvent(payload);
        }
    }
    
    private boolean validateSignature(String signature, ObjectNode payload) {
        String timestamp = payload.get("timestamp").asText();
        String oaId;
        if (payload.has("oa_id")) {
            oaId = payload.get("oa_id").asText();
        } else {
            JsonNode sender = payload.get("sender");
            oaId = sender.get("id").asText();
        }
        log.info("OaId: -{}", oaId);
        ZssApp zssApp = zssAppService.getZssAppByOaId(oaId);
        return !Objects.isNull(signature) && signature.equals(buildMac(zssApp.getAppId(), JacksonUtil.toJsonString(payload), timestamp, zssApp.getWebhookSecret()));
    }
    
    public static String buildMac(String appId, String data, String timeStamp, String secretKey) {
        String[] lstParams = new String[]{appId, data, timeStamp, secretKey};
        String mac = "mac=" + buildMacForAuthentication(lstParams);
        return mac;
    }
    
    public static String buildMacForAuthentication(String[] lstParams) {
        String res = "";
        for (String temp : lstParams) {
            res += temp;
        }
        return StringUtil.sha256(res);
    }
    
}
