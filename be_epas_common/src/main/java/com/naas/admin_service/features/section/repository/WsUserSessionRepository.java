package com.naas.admin_service.features.section.repository;

import com.naas.admin_service.features.section.model.WsUserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WsUserSessionRepository extends JpaRepository<WsUserSession, Long> {

    Optional<WsUserSession> findFirstByUsernameAndStatus(String username, String status);

    Optional<WsUserSession> findBySessionId(String sessionId);

    // Cùng user + cùng driverId + status
    Optional<WsUserSession> findFirstByUsernameAndStatusAndClientDriverId(
            String username,
            String status,
            String clientDriverId
    );

    // Tất cả session ACTIVE của user nhưng khác driverId
    List<WsUserSession> findAllByUsernameAndStatusAndClientDriverIdNot(
            String username,
            String status,
            String clientDriverId
    );
}
