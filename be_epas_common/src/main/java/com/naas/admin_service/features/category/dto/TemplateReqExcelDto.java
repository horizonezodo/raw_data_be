package com.naas.admin_service.features.category.dto;

import java.util.List;

import com.naas.admin_service.core.excel.dto.LabelExcelDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateReqExcelDto extends TemplateReqDto {
    private List<LabelExcelDto> label;
}
