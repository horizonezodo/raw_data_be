package com.naas.admin_service.features.section.service;

import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.section.model.WsUserSession;
import com.naas.admin_service.features.section.repository.WsUserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service thao tác với bảng WS_USER_SESSION.
 * Tập trung xử lý:
 *  - ACTIVE session theo (username, driverId)
 *  - PENDING session khi cần FORCE_LOGIN
 *  - Đánh dấu KICKED / LOGGED_OUT
 */
@Service
@RequiredArgsConstructor
public class WsUserSessionService {

    private final WsUserSessionRepository repo;

    // ---------- Query helpers ----------

    @Transactional(readOnly = true)
    public WsUserSession findActiveByUserAndDriver(String username, String driverId) {
        return repo.findFirstByUsernameAndStatusAndClientDriverId(username, Constant.ACTIVE, driverId)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public WsUserSession findAnyActiveByUser(String username) {
        return repo.findFirstByUsernameAndStatus(username, Constant.ACTIVE)
                .orElse(null);
    }

    // ---------- ACTIVE / PENDING ----------

    /**
     * Tạo hoặc update 1 session ACTIVE mới cho (user, driverId) hiện tại.
     */
    @Transactional
    public void createOrUpdateActiveSession(
            String username,
            String sessionId,
            String clientIp,
            String userAgent,
            String driverId
    ) {
        WsUserSession s = repo.findBySessionId(sessionId).orElseGet(WsUserSession::new);
        s.setUsername(username);
        s.setSessionId(sessionId);
        s.setClientIp(clientIp);
        s.setUserAgent(userAgent);
        s.setClientDriverId(driverId);
        s.setStatus(Constant.ACTIVE);
        s.setLastHeartbeat(LocalDateTime.now());
        repo.save(s);
    }

    /**
     * Tạo session ở trạng thái PENDING khi user đang chọn FORCE_LOGIN hay không.
     */
    @Transactional
    public void createPendingSession(
            String username,
            String sessionId,
            String clientIp,
            String userAgent,
            String driverId
    ) {
        WsUserSession s = repo.findBySessionId(sessionId).orElseGet(WsUserSession::new);
        s.setUsername(username);
        s.setSessionId(sessionId);
        s.setClientIp(clientIp);
        s.setUserAgent(userAgent);
        s.setClientDriverId(driverId);
        s.setStatus(Constant.PENDING);
        s.setLastHeartbeat(LocalDateTime.now());
        repo.save(s);
    }

    @Transactional
    public void cancelPending(String sessionId) {
        repo.findBySessionId(sessionId).ifPresent(s -> {
            if (Constant.PENDING.equals(s.getStatus())) {
                s.setStatus(Constant.LOGGED_OUT);
                s.setLastHeartbeat(LocalDateTime.now());
                repo.save(s);
            }
        });
    }

    /**
     * Đánh dấu LOGGED_OUT cho 1 session (dùng khi user tự đóng WS / logout).
     */
    @Transactional
    public void logoutBySessionId(String sessionId) {
        repo.findBySessionId(sessionId).ifPresent(s -> {
            if (!Constant.LOGGED_OUT.equals(s.getStatus()) && !Constant.KICKED.equals(s.getStatus())) {
                s.setStatus(Constant.LOGGED_OUT);
                s.setLastHeartbeat(LocalDateTime.now());
                repo.save(s);
            }
        });
    }

    /**
     * Đánh dấu tất cả session ACTIVE của user nhưng khác driverId là KICKED.
     * Trả về danh sách để bắn event Kafka.
     */
    @Transactional
    public List<WsUserSession> kickOtherDrivers(String username, String currentDriverId) {
        List<WsUserSession> others =
                repo.findAllByUsernameAndStatusAndClientDriverIdNot(username, Constant.ACTIVE, currentDriverId);

        others.forEach(s -> {
            s.setStatus(Constant.KICKED);
            s.setLastHeartbeat(LocalDateTime.now());
        });

        return repo.saveAll(others);
    }
}
