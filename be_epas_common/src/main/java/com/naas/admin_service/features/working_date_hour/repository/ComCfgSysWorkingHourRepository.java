package com.naas.admin_service.features.working_date_hour.repository;

import com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourDto;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingHour;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComCfgSysWorkingHourRepository extends BaseRepository<ComCfgSysWorkingHour> {

    boolean existsByDayOfWeekAndShiftCode(Integer dayOfWeek, String shiftCode);

    @Query("""
                SELECT new com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourDto(
                    w.id,
                    w.orgCode,
                    w.dayOfWeek,
                    shift.commonName,
                    working.commonName,
                    w.startTime,
                    w.endTime,
                    w.breakStartTime,
                    w.breakEndTime,
                    w.effectiveDate,
                    w.expiryDate,
                    w.isWorkingDay,
                    w.description
                )
                FROM ComCfgSysWorkingHour w
                LEFT JOIN CtgComCommon shift ON shift.commonCode = w.shiftCode
                LEFT JOIN CtgComCommon working ON working.commonCode = w.workingType
            
                WHERE w.isActive = 1
                  AND (
                        :keywords IS NULL
                        OR LOWER(w.orgCode) LIKE LOWER(CONCAT('%', :keywords, '%'))
                        OR LOWER(shift.commonName) LIKE LOWER(CONCAT('%', :keywords, '%'))
                        OR LOWER(working.commonName) LIKE LOWER(CONCAT('%', :keywords, '%'))
                      )
                ORDER BY w.modifiedDate DESC
            """)
    Page<SysWorkingHourDto> search(
            @Param("keywords") String keyword,
            Pageable pageable
    );

    Optional<ComCfgSysWorkingHour> findFirstByDayOfWeekAndShiftCode(Integer dayOfWeek, String shiftCode);

    Optional<ComCfgSysWorkingHour> findFirstByDayOfWeek(Integer dayOfWeek);

}
