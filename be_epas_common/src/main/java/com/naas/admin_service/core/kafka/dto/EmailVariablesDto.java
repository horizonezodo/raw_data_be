package com.naas.admin_service.core.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVariablesDto {
    private String processInstantCode;
    private String user;
    private String comment;
    private String mailCode;
}
