package com.naas.admin_service.core.kafka.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MailProcessRequestDto {
    private String emailTemplateCode;
    private List<String> userNameTo;
    private List<String> userNameCc;
    private String businessKey;
    private EmailVariablesDto emailVariables;
}
