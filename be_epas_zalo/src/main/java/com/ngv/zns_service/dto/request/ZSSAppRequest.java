package com.ngv.zns_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSAppRequest {
    private String appId;
    private String maDvi;
    private String appName;
    private String appSecret;
    private String webhookSecret;
    private String webhookVerificationFile;
    private String webhookVerificationContent;
    private String pcfWhUrl;
}
