package com.naas.admin_service.features.section.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.features.section.kafka.UserSessionEvent;
import com.naas.admin_service.features.section.model.WsUserSession;
import com.naas.admin_service.features.section.service.WsUserSessionService;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.SubProtocolCapable;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * WebSocketHandler đảm bảo:
 * - Mỗi user chỉ ACTIVE trên một driverId tại 1 thời điểm.
 * - Nếu login từ driver khác → gửi DUPLICATE_LOGIN để FE hỏi FORCE_LOGIN.
 * - Khi FORCE_LOGIN → đá các session khác (driver khác) trên toàn cluster (Kafka).
 * ✅ Quan trọng khi multi=true:
 * - tenantId được lưu trong session.attributes từ handshake
 * - mỗi lần xử lý WS message / gọi DB / send Kafka -> set TenantContext theo session.attributes
 * - producer interceptor của lib-core sẽ tự add header từ TenantContext
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SingleSessionWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable{

    private final WsUserSessionService userSessionService;
    private final WsLocalSessionRegistry localRegistry;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final MultitenancyProperties mtProps;

    @Value("${stat.websocket.session-topic}")
    private String userSessionTopic;

    @NotNull
    @Override
    public List<String> getSubProtocols() {
        return List.of("jwt"); // ✅ server chọn "jwt" để handshake OK
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        withTenant(session, () -> {
            WsCtx ctx = extractCtxOrClose(session);
            if (ctx == null) return;

            log.info("WS connection established: user={}, sessionId={}, driverId={}, tenant={}",
                    ctx.username, ctx.sessionId, ctx.driverId, TenantContext.getTenantId());

            localRegistry.registerSession(session);

            if (hasSameDriverActive(ctx)) {
                onSameDriverRefresh(session, ctx);
                return;
            }

            WsUserSession anyActive = userSessionService.findAnyActiveByUser(ctx.username);
            if (anyActive == null) {
                onFirstLogin(session, ctx);
            } else {
                onDuplicateLogin(session, ctx);
            }
        });
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {
        withTenant(session, () -> {
            try {
                JsonNode json = objectMapper.readTree(message.getPayload());
                String type = json.path("type").asText();

                switch (type) {
                    case "FORCE_LOGIN" -> handleForceLogin(session);
                    case "CANCEL_LOGIN" -> handleCancelLogin(session);
                    case "PING" -> handlePing(session);
                    default -> log.debug("Unknown WS message type={}", type);
                }
            } catch (Exception e) {
                log.error("WS handleTextMessage error", e);
            }
        });
    }

    private void handlePing(WebSocketSession session) {
        log.trace("WS PING sessionId={} tenant={}", session.getId(), TenantContext.getTenantId());
    }

    private void handleForceLogin(WebSocketSession newSession) {
        WsCtx ctx = extractCtxOrClose(newSession);
        if (ctx == null) return;

        log.info("FORCE_LOGIN requested user={} driverId={} sessionId={} tenant={}",
                ctx.username, ctx.driverId, ctx.sessionId, TenantContext.getTenantId());

        userSessionService.createOrUpdateActiveSession(
                ctx.username, ctx.sessionId, ctx.clientIp, ctx.userAgent, ctx.driverId
        );
        localRegistry.setActiveSession(ctx.username, ctx.sessionId);

        List<WsUserSession> kickedSessions =
                userSessionService.kickOtherDrivers(ctx.username, ctx.driverId);

        kickedSessions.forEach(s -> sendUserSessionEvent(s.getUsername(), s.getSessionId(), "KICKED"));

        sendJsonQuietly(newSession, "FORCE_LOGIN_OK", null);
    }

    private void handleCancelLogin(WebSocketSession session) {
        String sessionId = session.getId();
        log.info("CANCEL_LOGIN sessionId={} tenant={}", sessionId, TenantContext.getTenantId());

        userSessionService.cancelPending(sessionId);
        sendJsonQuietly(session, "LOGOUT", "Đăng nhập đã bị huỷ.");
        tryClose(session, CloseStatus.NORMAL);
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) {
        log.warn("WS transport error: sessionId={}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        withTenant(session, () -> {
            String sessionId = session.getId();
            log.info("WS connection closed sessionId={} status={} tenant={}",
                    sessionId, status, TenantContext.getTenantId());

            userSessionService.logoutBySessionId(sessionId);
            localRegistry.removeSession(sessionId);
        });
    }

    // ---------- Extracted small methods (giảm Cognitive Complexity) ----------

    private record WsCtx(
            String username,
            String clientIp,
            String userAgent,
            String driverId,
            String sessionId
    ) {}

    private WsCtx extractCtxOrClose(WebSocketSession session) {
        String username = (String) session.getAttributes().get("username");
        if (username == null || username.isBlank()) {
            log.warn("WS connection rejected: missing username");
            tryClose(session, CloseStatus.BAD_DATA);
            return null;
        }

        return new WsCtx(
                username,
                (String) session.getAttributes().get("clientIp"),
                (String) session.getAttributes().get("userAgent"),
                (String) session.getAttributes().get("driverId"),
                session.getId()
        );
    }

    private boolean hasSameDriverActive(WsCtx ctx) {
        WsUserSession sameDriverActive = userSessionService.findActiveByUserAndDriver(ctx.username, ctx.driverId);
        return sameDriverActive != null;
    }

    private void onSameDriverRefresh(WebSocketSession session, WsCtx ctx) {
        userSessionService.createOrUpdateActiveSession(
                ctx.username, ctx.sessionId, ctx.clientIp, ctx.userAgent, ctx.driverId
        );
        localRegistry.setActiveSession(ctx.username, ctx.sessionId);
        sendJsonQuietly(session, "LOGIN_SUCCESS", null);
    }

    private void onFirstLogin(WebSocketSession session, WsCtx ctx) {
        userSessionService.createOrUpdateActiveSession(
                ctx.username, ctx.sessionId, ctx.clientIp, ctx.userAgent, ctx.driverId
        );
        localRegistry.setActiveSession(ctx.username, ctx.sessionId);

        sendUserSessionEvent(ctx.username, ctx.sessionId, "ACTIVE");
        sendJsonQuietly(session, "LOGIN_SUCCESS", null);
    }

    private void onDuplicateLogin(WebSocketSession session, WsCtx ctx) {
        userSessionService.createPendingSession(
                ctx.username, ctx.sessionId, ctx.clientIp, ctx.userAgent, ctx.driverId
        );
        localRegistry.setPendingSession(ctx.username, ctx.sessionId);

        sendJsonQuietly(session, "DUPLICATE_LOGIN",
                "Tài khoản đang đăng nhập ở thiết bị khác. Bạn có muốn đăng nhập và đăng xuất phiên còn lại không?");
    }

    // ---------- Helpers ----------

    private void withTenant(WebSocketSession session, Runnable fn) {
        if (!mtProps.isEnabled()) {
            fn.run();
            return;
        }

        Object t = session.getAttributes().get("tenantId");
        String tenantId = (t == null) ? null : t.toString();

        if (tenantId == null || tenantId.isBlank()) {
            // custom runtime exception cũng OK; IllegalArgumentException vẫn chấp nhận được
            throw new MissingTenantInWebSocketSessionException();
        }

        TenantContext.setTenantId(tenantId.trim());
        try {
            fn.run();
        } finally {
            TenantContext.clear();
        }
    }

    private void sendJsonQuietly(WebSocketSession session, String type, String message) {
        try {
            sendJson(session, type, message);
        } catch (IOException e) {
            // Không throw RuntimeException generic nữa
            log.warn("WS sendJson failed: sessionId={} type={}", session.getId(), type, e);
            tryClose(session, CloseStatus.SERVER_ERROR);
        }
    }

    private void sendJson(WebSocketSession session, String type, String message) throws IOException {
        if (!session.isOpen()) return;
        var root = objectMapper.createObjectNode().put("type", type);
        if (message != null) root.put("message", message);
        session.sendMessage(new TextMessage(root.toString()));
    }

    private void sendUserSessionEvent(String username, String sessionId, String status) {
        try {
            UserSessionEvent event = UserSessionEvent.builder()
                    .username(username)
                    .sessionId(sessionId)
                    .status(status)
                    .eventTime(LocalDateTime.now())
                    .build();

            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(userSessionTopic, username, json);
        } catch (JsonProcessingException e) {
            log.error("Error serialize UserSessionEvent", e);
        } catch (Exception e) {
            log.error("Error sending UserSessionEvent to Kafka", e);
        }
    }

    private void tryClose(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
        } catch (IOException e) {
            // Intentionally ignore: connection may already be closed by client/infra
            log.debug("WS close ignored: sessionId={}", session.getId(), e);
        }
    }

    // ---------- Custom exception (để Sonar khỏi chê generic) ----------
    static class MissingTenantInWebSocketSessionException extends RuntimeException {
        MissingTenantInWebSocketSessionException() {
            super("Missing tenantId in WebSocket session.attributes");
        }
    }

}