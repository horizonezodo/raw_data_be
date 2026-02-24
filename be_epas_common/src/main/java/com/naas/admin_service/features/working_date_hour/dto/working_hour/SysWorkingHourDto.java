package com.naas.admin_service.features.working_date_hour.dto.working_hour;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysWorkingHourDto {
    private Long id;

    @NotBlank(message = "ORG_CODE là bắt buộc")
    private String orgCode;

    @NotNull(message = "DAY_OF_WEEK là bắt buộc")
    private Integer dayOfWeek;

    @NotBlank(message = "SHIFT_CODE là bắt buộc")
    private String shiftCode;

    @NotBlank(message = "WORKING_TYPE là bắt buộc")
    private String workingType;

    private String startTime;

    private String endTime;

    private String breakStartTime;

    private String breakEndTime;

    private LocalDate effectiveDate;

    private LocalDate expiryDate;

    private Integer isWorkingDay;

    private String description;
}
