package com.naas.admin_service.features.working_date_hour.dto.working_date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingDateResDto {

    private String orgCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    private Integer baseIsWorkingDay;

    private Integer isWorkingDay;

    private String workingRegisterType;

    private String startTime;

    private String endTime;

    private String holidayType;

    private String holidayName;

    private Integer isGenerated;

    private String reason;

}
