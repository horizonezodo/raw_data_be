package com.naas.admin_service.features.working_date_hour.dto.working_hour;

import com.naas.admin_service.core.contants.ExcelColumns;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SysWorkingHourExcel {
    @ExcelColumn(ExcelColumns.HEADER_ORG_CODE)
    private String orgCode;
    @ExcelColumn(ExcelColumns.HEADER_DAY_OF_WEEK)
    private Integer dayOfWeek;
    @ExcelColumn(ExcelColumns.HEADER_SHIFT_CODE)
    private String shiftCode;
    @ExcelColumn(ExcelColumns.HEADER_IS_WORKING_DAY)
    private String isWorkingDay;
    @ExcelColumn(ExcelColumns.HEADER_WORKING_TYPE)
    private String workingType;
    @ExcelColumn(ExcelColumns.HEADER_WORKING_TIME)
    private String workingTime;
    @ExcelColumn(ExcelColumns.HEADER_EFFECTIVE_DATE)
    private LocalDate effectiveDate;
}
