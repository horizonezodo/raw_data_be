package com.naas.admin_service.features.mail.dto.comlogmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComLogMailDto {
    private String processInstanceCode;
    private String mailTempCode;
    private String mailSubject;
    private Timestamp sendTime;
    private String mailStatus;
    private String toEmails;
    private String ccEmails;
    private String bccEmails;
    private String description;
    private String mailBody;
}
