package com.naas.admin_service.features.working_date_hour.dto.holiday;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HolidaySearch {
    private String keyword;
    private String orgCode;
    private String year;
    private String holidayType;
    private Integer page;
    private Integer size;
}
