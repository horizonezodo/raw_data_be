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
@Table(name = "COM_CFG_SYS_WORKING_HOUR")
public class ComCfgSysWorkingHour extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "DAY_OF_WEEK")
    private Integer dayOfWeek;

    @Column(name = "SHIFT_CODE", length = 64)
    private String shiftCode;

    @Column(name = "WORKING_TYPE", length = 64)
    private String workingType;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    @Column(name = "START_TIME", length = 8)
    private String startTime;

    @Column(name = "END_TIME", length = 8)
    private String endTime;

    @Column(name = "BREAK_START_TIME", length = 8)
    private String breakStartTime;

    @Column(name = "BREAK_END_TIME", length = 8)
    private String breakEndTime;

    @Column(name = "IS_WORKING_DAY")
    private Integer isWorkingDay;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
