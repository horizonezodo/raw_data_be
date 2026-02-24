package com.naas.admin_service.features.section.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event gửi qua Kafka để sync trạng thái WS user giữa các node.
 * Ví dụ:
 *  - ACTIVE: user được login trên sessionId này
 *  - KICKED: sessionId này phải bị đá ra (gửi LOGOUT cho client)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionEvent {

    private String username;
    private String sessionId;
    private String status;    // ACTIVE, KICKED, LOGGED_OUT ...
    private LocalDateTime eventTime;
}
