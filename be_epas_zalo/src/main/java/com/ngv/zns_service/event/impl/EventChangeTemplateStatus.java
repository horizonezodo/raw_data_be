package com.ngv.zns_service.event.impl;

import com.ngv.zns_service.model.entity.ZssApp;
import com.ngv.zns_service.service.ZSSAppService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.event.EventHandler;
import com.ngv.zns_service.service.ZSSMauZNZService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Component
public class EventChangeTemplateStatus implements EventHandler {

    private final String eventName = "change_template_status";

    private final WebClient webClient;
    private final ZSSMauZNZService ZssMauZnsService;
    private final ZSSAppService zssAppService;

    @Override
    public void handleEvent(ObjectNode data) {
        try {
            log.info("Sending event data to API: {}", data);

            ZssApp zssApp = zssAppService.getById(data.path("app_id").asText(null));
            if (zssApp == null) {
                log.error("No ZSS App found for event!");
            }
            String pcfWhUrl = zssApp.getPcfWhUrl() + "/TempSyncZNS";

            ZssMauZnsService.changeTemplateStatus(data);

            String response = webClient.post()
                    .uri(pcfWhUrl)
                    .header("Content-Type", "application/json")
                    .bodyValue(data) // Gửi payload JSON trực tiếp
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Chờ kết quả trả về (synchronous)

            log.info("Response from API: {}", response);
        } catch (Exception e) {
            log.error("Error while sending event data: ", e);
        }
    }

    @Override
    public String getEventName() {
        return eventName;
    }
}
