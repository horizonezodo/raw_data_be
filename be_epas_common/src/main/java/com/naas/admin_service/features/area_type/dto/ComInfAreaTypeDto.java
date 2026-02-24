package com.naas.admin_service.features.area_type.dto;

import com.naas.admin_service.core.contants.ExcelColumns;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComInfAreaTypeDto {


    @ExcelColumn(value = ExcelColumns.AREA_TYPE_CODE)
    private String areaTypeCode;

    @ExcelColumn(value = ExcelColumns.AREA_TYPE_NAME)
    private String areaTypeName;

    @ExcelColumn(value = ExcelColumns.DESCRIPTION)
    private String description;


    public ComInfAreaTypeDto(String areaTypeCode, String areaTypeName, String description) {
        this.areaTypeName = areaTypeName;
        this.areaTypeCode = areaTypeCode;

        this.description = description;
    }

    private List<String> labels;
}
