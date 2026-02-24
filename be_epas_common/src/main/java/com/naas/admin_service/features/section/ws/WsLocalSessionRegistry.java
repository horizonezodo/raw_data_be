package com.naas.admin_service.features.section.ws;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry lưu các WebSocketSession đang mở trên LOCAL NODE.
 * Không lưu trong DB, chỉ dùng trong memory:
 *  - sessionId -> WebSocketSession
 *  - username  -> activeSessionId
 *  - username  -> pendingSessionId
 */
@Component
@Getter
public class WsLocalSessionRegistry {

    /** sessionId -> WebSocketSession */
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /** username -> active sessionId */
    private final Map<String, String> activeSessions = new ConcurrentHashMap<>();

    /** username -> pending sessionId */
    private final Map<String, String> pendingSessions = new ConcurrentHashMap<>();

    public void registerSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
        // Nếu muốn, có thể remove luôn khỏi active/pending
        activeSessions.entrySet().removeIf(e -> sessionId.equals(e.getValue()));
        pendingSessions.entrySet().removeIf(e -> sessionId.equals(e.getValue()));
    }

    public void setActiveSession(String username, String sessionId) {
        activeSessions.put(username, sessionId);
        pendingSessions.remove(username);
    }

    public void setPendingSession(String username, String sessionId) {
        pendingSessions.put(username, sessionId);
    }

    public String getActiveSessionId(String username) {
        return activeSessions.get(username);
    }

    public String getPendingSessionId(String username) {
        return pendingSessions.get(username);
    }

    public WebSocketSession getSessionById(String sessionId) {
        return sessions.get(sessionId);
    }
}
