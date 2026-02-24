package com.naas.admin_service.features.mail.dto.comcfgmailtemplate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgMailTemplateRequest {
    @NotBlank(message = "Mã code mail không được để trống")
    @Pattern(
            regexp = "^[^/ .].*$",
            message = "Mã code mail không được bắt đầu bằng '/', '.' hoặc khoảng trắng"
    )
    private String mailTemplateCode;

    private String mailTemplateName;
    private String mailSubject;
    private String mailBody;
    private String toEmails;
    private String ccEmails;
    private String bccEmails;
}
