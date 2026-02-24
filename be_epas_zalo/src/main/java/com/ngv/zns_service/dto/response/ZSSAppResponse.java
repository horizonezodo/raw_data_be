package com.ngv.zns_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZSSAppResponse {
    private String appId;
    private String maDvi;
    private String tenDvi;
    private String appName;
    private String tthaiNvu;
    private String appSecret;
    private String webhookSecret;
    private String webhookVerificationFile;
    private String webhookVerificationContent;
    private String pcfWhUrl;
}
