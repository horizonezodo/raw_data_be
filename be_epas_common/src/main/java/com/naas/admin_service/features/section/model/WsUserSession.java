package com.naas.admin_service.features.section.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "WS_USER_SESSION")
public class WsUserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "SESSION_ID", nullable = false)
    private String sessionId;

    @Column(name = "STATUS", nullable = false)
    private String status; // ACTIVE / PENDING / LOGGED_OUT / KICKED

    @Column(name = "LOGIN_TIME")
    private LocalDateTime loginTime;

    @Column(name = "LAST_HEARTBEAT")
    private LocalDateTime lastHeartbeat;

    @Column(name = "CLIENT_IP")
    private String clientIp;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Column(name = "CLIENT_DRIVER_ID")
    private String clientDriverId; // 🔹 thêm vào đây
}

