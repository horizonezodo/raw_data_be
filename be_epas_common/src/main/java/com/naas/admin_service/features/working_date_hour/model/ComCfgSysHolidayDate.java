package com.naas.admin_service.features.working_date_hour.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COM_CFG_SYS_HOLIDAY_DATE")
public class ComCfgSysHolidayDate extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "HOLIDAY_DATE")
    private LocalDate holidayDate;

    @Column(name = "HOLIDAY_TYPE", length = 64)
    private String holidayType;

    @Column(name = "HOLIDAY_NAME", length = 128)
    private String holidayName;

    @Column(name = "IS_REPEAT_ANNUAL", length = 1)
    private int isRepeatAnnual;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
