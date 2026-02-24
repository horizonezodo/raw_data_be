package com.naas.admin_service.features.working_date_hour.dto.holiday;

import lombok.Data;

@Data
public class DayWorkInfo {
    final int baseIsWorkingDay;
    final String holidayType;
    final String holidayName;

    public DayWorkInfo(int baseIsWorkingDay, String holidayType, String holidayName) {
        this.baseIsWorkingDay = baseIsWorkingDay;
        this.holidayType = holidayType;
        this.holidayName = holidayName;
    }
}

