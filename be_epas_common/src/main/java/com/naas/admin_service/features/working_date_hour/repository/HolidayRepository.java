package com.naas.admin_service.features.working_date_hour.repository;

import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayResDTO;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysHolidayDate;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends BaseRepository<ComCfgSysHolidayDate> {
    @Query("""
                select new com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayResDTO(
                    c.id, c.orgCode, o.orgName, c.holidayDate, c.holidayType, c.holidayName,
                    c.isRepeatAnnual, c.isActive, c.recordStatus, c.description, c.modifiedDate )
                from ComCfgSysHolidayDate c
                left join ComInfOrganization o on o.orgCode = c.orgCode
                where ( :keyword IS NULL OR
                                LOWER(c.holidayType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(c.holidayName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(c.orgCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                            ) AND
                      ( :orgCode IS NULL OR
                                LOWER(c.orgCode) LIKE LOWER(CONCAT('%', :orgCode, '%'))
                            ) AND
                      ( :holidayType IS NULL OR
                                LOWER(c.holidayType) LIKE LOWER(CONCAT('%', :holidayType, '%'))
                            ) AND
                      ( :year IS NULL OR EXTRACT(YEAR FROM c.holidayDate) = :year ) AND c.isActive = 1
                order by c.modifiedDate desc
            """)
    Page<HolidayResDTO> search(@Param("keyword") String keyword,
                               @Param("orgCode") String orgCode,
                               @Param("year") Integer year,
                               @Param("holidayType") String holidayType,
                               Pageable pageable);

    List<ComCfgSysHolidayDate> findAllByHolidayDate(LocalDate holidayDate);
    Optional<ComCfgSysHolidayDate> findByHolidayDate(LocalDate holidayDate);

    List<ComCfgSysHolidayDate> findByOrgCodeAndHolidayDateBetween(String orgCode, LocalDate start, LocalDate end);

    @Query("""
                select new com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayResDTO(
                    c.id, c.orgCode, o.orgName, c.holidayDate, c.holidayType, c.holidayName,
                    c.isRepeatAnnual, c.isActive, c.recordStatus, c.description, c.modifiedDate )
                from ComCfgSysHolidayDate c
                left join ComInfOrganization o on o.orgCode = c.orgCode
                where ( :keyword IS NULL OR
                                LOWER(c.holidayType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(c.holidayName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(c.orgCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                            ) AND
                      ( :orgCode IS NULL OR
                                LOWER(c.orgCode) LIKE LOWER(CONCAT('%', :orgCode, '%'))
                            ) AND
                      ( :holidayType IS NULL OR
                                LOWER(c.holidayType) LIKE LOWER(CONCAT('%', :holidayType, '%'))
                            ) AND
                      ( :year IS NULL OR EXTRACT(YEAR FROM c.holidayDate) = :year ) AND c.isActive = 1
                order by c.modifiedDate desc
            """)
    Page<HolidayResDTO> getDataExportAll(@Param("keyword") String keyword,
                               @Param("orgCode") String orgCode,
                               @Param("year") Integer year,
                               @Param("holidayType") String holidayType,
                               Pageable pageable);
}
