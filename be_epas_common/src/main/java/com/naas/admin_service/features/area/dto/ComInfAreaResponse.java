package com.naas.admin_service.features.area.dto;

import com.naas.admin_service.core.contants.ExcelColumns;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComInfAreaResponse {
    private Long id;
    @ExcelColumn(value = ExcelColumns.AREA_CODE)
    private String areaCode;
    @ExcelColumn(value = ExcelColumns.AREA_NAME)
    private String areaName;
    @ExcelColumn(value = ExcelColumns.HEADER_ORG_CODE)
    private String orgName;
    @ExcelColumn(value = ExcelColumns.AREA_TYPE_NAME)
    private String areaTypeName;
    @ExcelColumn(value = ExcelColumns.WARD_NAME)
    private String wardName;
}
