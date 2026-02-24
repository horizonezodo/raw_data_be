package com.naas.admin_service.core.excel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import com.naas.admin_service.core.excel.dto.LabelExcelDto;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportExcelRequest extends SearchFilterRequest {
    private List<LabelExcelDto> labels;
}
