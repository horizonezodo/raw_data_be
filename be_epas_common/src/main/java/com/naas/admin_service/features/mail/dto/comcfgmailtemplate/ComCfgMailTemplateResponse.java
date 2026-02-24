package com.naas.admin_service.features.mail.dto.comcfgmailtemplate;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgMailTemplateResponse {
    private Long id;
    private String mailTemplateCode;
    private String mailTemplateName;
    private String mailSubject;
    private String mailBody;
    private String toEmails;
    private String ccEmails;
    private String bccEmails;
    private String mailCode;
}
