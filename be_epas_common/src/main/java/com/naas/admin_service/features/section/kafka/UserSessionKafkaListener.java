package com.naas.admin_service.features.section.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.features.section.service.WsUserSessionService;
import com.naas.admin_service.features.section.ws.WsLocalSessionRegistry;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Lắng nghe UserSessionEvent từ Kafka để:
 * - Đá các session KICKED trên node hiện tại (gửi LOGOUT cho client)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserSessionKafkaListener {

    private final WsLocalSessionRegistry localRegistry;
    private final WsUserSessionService userSessionService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${stat.websocket.session-topic}",
            groupId = "${stat.websocket.group-id}",
            containerFactory = "userSessionEventListenerContainerFactory"
    )
    public void onUserSessionEvent(@Payload UserSessionEvent event) {

        String tenantId = TenantContext.getTenantId();

        try {
            log.info("Kafka received UserSessionEvent tenant={} event={}", tenantId, event);

            if (!"KICKED".equalsIgnoreCase(event.getStatus())) {
                return;
            }

            String sessionId = event.getSessionId();
            WebSocketSession session = localRegistry.getSessionById(sessionId);
            if (session == null) {
                log.info("No local WS session found for sessionId={} (probably on another node), tenant={}", sessionId, tenantId);
                return;
            }

            sendLogoutAndCloseSession(session, sessionId);
            userSessionService.logoutBySessionId(sessionId);
            localRegistry.removeSession(sessionId);

        } catch (Exception ex) {
            log.error("Error handling UserSessionEvent tenant={} event={}", tenantId, event, ex);
        }
    }

    private void sendLogoutAndCloseSession(WebSocketSession session, String sessionId) {
        try {
            var msg = objectMapper.createObjectNode()
                    .put("type", "LOGOUT")
                    .put("message", "Phiên đăng nhập đã bị đăng xuất vì bạn đăng nhập từ thiết bị khác.");

            session.sendMessage(new TextMessage(msg.toString()));
            session.close();

        } catch (IOException e) {
            log.warn("Error sending LOGOUT to sessionId={}", sessionId, e);
        }
    }
}
