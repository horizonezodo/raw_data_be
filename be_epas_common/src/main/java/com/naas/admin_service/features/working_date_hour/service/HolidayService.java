package com.naas.admin_service.features.working_date_hour.service;

import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayDTO;
import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayResDTO;
import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidaySearch;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysHolidayDate;
import com.ngvgroup.bpm.core.persistence.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface HolidayService extends BaseService<ComCfgSysHolidayDate, HolidayDTO> {
    Page<HolidayResDTO> search(HolidaySearch search, Pageable pageable);
    List<HolidayDTO> addAll(List<HolidayDTO> listHolidays);
    boolean existHolidayDate(LocalDate holidayDate);
    Page<HolidayResDTO> exportData(HolidaySearch search, Pageable pageable);
}
