package com.naas.admin_service.features.working_date_hour.dto.working_date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkingDateSearchRequest {
    private String orgCode;
    private int year;
    private String month;
}

