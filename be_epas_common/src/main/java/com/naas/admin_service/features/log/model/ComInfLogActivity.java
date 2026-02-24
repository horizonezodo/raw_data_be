package com.naas.admin_service.features.log.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "COM_INF_LOG_ACTIVITY")
public class ComInfLogActivity {

    // 1) ACTIVITY_ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID", nullable = false)
    private Long activityId;

    // 2) EVENT_TIME
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EVENT_TIME")
    private Date eventTime;

    // 3) STATUS_CODE
    @Column(name = "STATUS_CODE", length = 64)
    private String statusCode;

    // 4) USERNAME
    @Column(name = "USERNAME", length = 256)
    private String username;

    // 5) FUNCTION_CODE
    @Column(name = "FUNCTION_CODE", length = 128)
    private String functionCode;

    // 6) ACTION_NAME
    @Column(name = "ACTION_NAME", length = 256)
    private String actionName;

    // 7) REQUEST_ID
    @Column(name = "REQUEST_ID", length = 128)
    private String requestId;

    // 8) REQUEST_URL
    @Column(name = "REQUEST_URL", length = 512)
    private String requestUrl;

    // 9) METHOD_CODE
    @Column(name = "METHOD_CODE", length = 16)
    private String methodCode;

    // 10) SERVICE_NAME
    @Column(name = "SERVICE_NAME", length = 256)
    private String serviceName;

    // 11) CLIENT_IP
    @Column(name = "CLIENT_IP", length = 64)
    private String clientIp;

    // 12) CLIENT_NAME
    @Column(name = "CLIENT_NAME", length = 256)
    private String clientName;

    // 13) BROWSER_INFO
    @Column(name = "BROWSER_INFO", length = 512)
    private String browserInfo;

    // 14) DURATION_TIME
    @Column(name = "DURATION_TIME")
    private Long durationTime;

    // 15) REQUEST_PAYLOAD
    @Lob
    @Column(name = "REQUEST_PAYLOAD")
    private String requestPayload;
}