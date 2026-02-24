package com.naas.admin_service.features.working_date_hour.dto.holiday;
import com.naas.admin_service.core.contants.ExcelColumns;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HolidayResDTO {

    private Long id;
    private String orgCode;
    @ExcelColumn(ExcelColumns.ORG_NAME)
    private String orgName;
    @ExcelColumn(ExcelColumns.HOLIDAY_DATE)
    private LocalDate holidayDate;
    @ExcelColumn(ExcelColumns.HOLIDAY_TYPE)
    private String holidayType;
    @ExcelColumn(ExcelColumns.HOLIDAY_NAME)
    private String holidayName;
    @ExcelColumn(ExcelColumns.IS_REPEAT_ANNUAL)
    private int isRepeatAnnual;
    private int isActive;
    private String recordStatus = "approval";
    @ExcelColumn(ExcelColumns.DESCRIPTION)
    private String description;
    private Timestamp modifiedDate;
}
