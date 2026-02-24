package com.ngvgroup.bpm.core.logging.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor      // <– BẮT BUỘC cho Jackson
@AllArgsConstructor
public class ActivityLogMessage {

    // REQUEST_ID – dùng để join với audit log nếu cần
    private String requestId;

    // EVENT_TIME – Thời gian hành động
    private Date eventTime;

    // STATUS_CODE – 200 / 500 / SUCCESS / FAIL...
    private String statusCode;

    // USERNAME – tên đăng nhập
    private String username;

    // FUNCTION_CODE – mã chức năng
    private String functionCode;

    // ACTION_NAME – mô tả hành động (VD: "Tìm kiếm", "Thêm mới")
    private String actionName;

    // REQUEST_URL – API / URL được gọi
    private String requestUrl;

    // METHOD_CODE – GET / POST / PUT / DELETE
    private String methodCode;

    // SERVICE_NAME – tên service/app
    private String serviceName;

    // CLIENT_IP – IP máy trạm
    private String clientIp;

    // CLIENT_NAME – tên máy trạm
    private String clientName;

    // BROWSER_INFO – User-Agent
    private String browserInfo;

    // DURATION_TIME – thời gian xử lý (ms)
    private Long durationTime;

    // REQUEST_PAYLOAD – payload/body/param request (JSON string)
    private String requestPayload;
}
