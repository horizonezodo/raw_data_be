package com.naas.admin_service.features.working_date_hour.model;


import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.working_date_hour.dto.holiday.DayWorkInfo;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COM_CFG_SYS_WORKING_DATE")
public class ComCfgSysWorkingDate extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "WORK_DATE")
    private LocalDate workDate;

    @Column(name = "WORKING_REGISTER_TYPE", length = 64)
    private String workingRegisterType;

    @Column(name = "BASE_IS_WORKING_DAY", length = 1)
    private Integer baseIsWorkingDay;

    @Column(name = "IS_WORKING_DAY", length = 1, nullable = false)
    private Integer isWorkingDay;

    @Column(name = "START_TIME", length = 8)
    private String startTime;

    @Column(name = "END_TIME", length = 8)
    private String endTime;

    @Column(name = "HOLIDAY_TYPE", length = 64)
    private String holidayType;

    @Column(name = "HOLIDAY_NAME", length = 128)
    private String holidayName;

    @Column(name = "IS_GENERATED", length = 1, nullable = false)
    private Integer isGenerated;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    public static ComCfgSysWorkingDate buildEntity(
            String orgCode,
            LocalDate date,
            DayWorkInfo base,
            String startTime,
            String endTime
    ) {
        ComCfgSysWorkingDate entity = new ComCfgSysWorkingDate();
        entity.setOrgCode(orgCode);
        entity.setWorkDate(date);
        entity.setBaseIsWorkingDay(base.getBaseIsWorkingDay());
        entity.setIsWorkingDay(base.getBaseIsWorkingDay());
        entity.setHolidayType(base.getHolidayType());
        entity.setHolidayName(base.getHolidayName());
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);

        entity.setIsGenerated(0);
        entity.setIsDelete(0);
        entity.setRecordStatus(Constant.APPROVAL);
        entity.setCreatedBy(Constant.SYSTEM);
        entity.setModifiedBy(Constant.SYSTEM);
        entity.setApprovedBy(Constant.SYSTEM);

        return entity;
    }

}
