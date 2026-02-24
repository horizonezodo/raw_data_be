package com.naas.admin_service.features.working_date_hour.repository;

import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingDate;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ComCfgSysWorkingDateRepository extends BaseRepository<ComCfgSysWorkingDate> {

    Optional<ComCfgSysWorkingDate> findByOrgCodeAndWorkDate(String orgCode, LocalDate workDate);
    @Modifying
    @Transactional
    @Query("UPDATE ComCfgSysWorkingDate c SET c.workingRegisterType = null , c.isWorkingDay = 0, " +
            "c.holidayType = :holidayType, " +
            "c.holidayName = :holidayName, c.isGenerated = 0, c.startTime = null , c.endTime = null " +
            "WHERE c.orgCode = :orgCode and c.workDate = :workDate " )
    void updateWorkingDate(@Param("orgCode") String orgCode,
                        @Param("workDate") LocalDate workDate,
                        @Param("holidayType") String holidayType,
                        @Param("holidayName") String holidayName);

    @Modifying
    @Transactional
    @Query("UPDATE ComCfgSysWorkingDate c SET c.isWorkingDay = 1," +
            "c.startTime = :startTime, c.endTime = :endTime," +
            "c.holidayType = null, c.holidayName = null " +
            "WHERE c.orgCode = :orgCode and c.workDate = :workDate " )
    void updateWorkingDateWithNormalDay(
            @Param("orgCode") String orgCode,
            @Param("workDate") LocalDate workDate,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);

    @Modifying
    @Transactional
    @Query("UPDATE ComCfgSysWorkingDate c SET c.isWorkingDay = 0," +
            "c.startTime = null, c.endTime = null, c.isGenerated = 0, " +
            "c.holidayType = 'CUOI_TUAN', c.holidayName = :holidayName " +
            "WHERE c.orgCode = :orgCode and c.workDate = :workDate " )
    void updateWorkingDatWithHolidayDay(
            @Param("orgCode") String orgCode,
            @Param("workDate") LocalDate workDate,
            @Param("holidayName") String holidayName);

    void deleteByOrgCodeAndWorkDateBetween(String orgCode, LocalDate from, LocalDate to);

    List<ComCfgSysWorkingDate> findByOrgCodeAndWorkDateBetween(String orgCode, LocalDate start, LocalDate end);
}
