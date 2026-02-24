package com.naas.admin_service.features.working_date_hour.dto.working_date;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgSysWorkingDateDto {

    private String orgCode;

    private LocalDate workDate;

    private String workingRegisterType;

    private Integer baseIsWorkingDay;

    private Integer isWorkingDay;

    private String startTime;

    private String endTime;

    private String holidayType;

    private String holidayName;

    @Min(value = 0, message = "Giá trị tự động sinh của hệ thống phải là 0 hoặc 1")
    @Max(value = 1, message = "Giá trị tự động sinh của hệ thống phải là 0 hoặc 1")
    @NotNull
    private Integer isGenerated;

    private String reason;
}
