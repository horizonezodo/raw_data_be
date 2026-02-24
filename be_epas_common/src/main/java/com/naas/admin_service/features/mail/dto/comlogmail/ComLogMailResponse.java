package com.naas.admin_service.features.mail.dto.comlogmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComLogMailResponse {
    private Long id;
    private String processInstanceCode;
    private String mailTemplateCode;
    private String mailSubject;
    private Timestamp sendTime;
    private String mailStatus;
}
