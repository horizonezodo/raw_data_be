package com.naas.admin_service.features.mail.dto.comcfgmailfrom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComCfgMailFromResponse {
    private Long id;
    private String mailCode;
    private String mailFrom;
    private String mailFromName;
    private String mailPassword;
    private String host;
    private Integer port;
}
