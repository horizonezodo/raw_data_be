package com.naas.admin_service.features.mail.dto.comcfgmailfrom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComCfgMailFromDto {
    private String orgCode;
    private String mailCode;
    private String mailFrom;
    private String mailFromName;
    private String mailPassword;
    private Boolean isEncrypted;
    private String host;
    private Integer port;
    private String mailProtocol;
    private Boolean isUseSsl;
    private List<Long> templateIds;
    private List<ComCfgMailTemplateResponse> templateResponses;

}
