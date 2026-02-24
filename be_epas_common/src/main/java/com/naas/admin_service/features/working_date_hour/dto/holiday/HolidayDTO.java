package com.naas.admin_service.features.working_date_hour.dto.holiday;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HolidayDTO {

    private Long id;

    @NotBlank(message = "Mã tổ chức không được để trống!")
    @Size(max = 64, message = "Mã tổ chức không được vượt quá 64 kí tự!")
    private String orgCode;

    private LocalDate holidayDate;

    private String holidayType;

    private String holidayName;

    @Min(value = 0, message = "Trạng thái hoạt động phải là 0 hoặc 1")
    @Max(value = 1, message = "Trạng thái hoạt động phải là 0 hoặc 1")
    private int isRepeatAnnual;

    private String recordStatus = "approval";

    private String description;
}
