package com.naas.admin_service.features.mail.dto.comlogmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import com.naas.admin_service.core.excel.dto.LabelExcelDto;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComLogMailSearchRequest extends SearchFilterRequest {
    private String processInstanceCode;
    private String mailTemplateCode;
    private String mailSubject;
    private String mailStatus;
    private String fromDate;
    private String toDate;
    List<LabelExcelDto> labels;
}
