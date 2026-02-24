package com.naas.admin_service.features.working_date_hour.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayDTO;
import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayResDTO;
import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidaySearch;
import com.naas.admin_service.features.working_date_hour.mapper.HolidayMapper;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysHolidayDate;
import com.naas.admin_service.features.working_date_hour.repository.HolidayRepository;
import com.naas.admin_service.features.working_date_hour.service.HolidayService;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingDate;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingHour;
import com.naas.admin_service.features.working_date_hour.repository.ComCfgSysWorkingDateRepository;
import com.naas.admin_service.features.working_date_hour.repository.ComCfgSysWorkingHourRepository;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HolidayServiceImpl
        extends BaseServiceImpl<ComCfgSysHolidayDate, HolidayDTO>
        implements HolidayService {
    private final HolidayRepository holidayRepository;
    private final ComCfgSysWorkingDateRepository workingDateRepository;
    private final ComCfgSysWorkingHourRepository workingHourRepository;

    public HolidayServiceImpl(HolidayRepository holidayRepository,
                              HolidayMapper holidayMapper,
                              ComCfgSysWorkingDateRepository workingDateRepository,
                              ComCfgSysWorkingHourRepository workingHourRepository
                              ) {
        super(holidayRepository, holidayMapper);
        this.holidayRepository = holidayRepository;
        this.workingDateRepository = workingDateRepository;
        this.workingHourRepository = workingHourRepository;
    }

    @Override
    public Page<HolidayResDTO> search(HolidaySearch search, Pageable pageable) {
        String keyword = search.getKeyword();
        String orgCode = search.getOrgCode();
        Integer year = parseYear(search.getYear());
        String holidayType = search.getHolidayType();

        return holidayRepository.search(keyword, orgCode, year, holidayType, pageable);
    }

    @Override
    @Transactional
    public List<HolidayDTO> addAll(List<HolidayDTO> listHolidayDTOs) {
        List<ComCfgSysHolidayDate> result = new ArrayList<>();

        for (HolidayDTO holidayDTO : listHolidayDTOs) {
            List<ComCfgSysHolidayDate> existHolidayDate = holidayRepository.findAllByHolidayDate(holidayDTO.getHolidayDate());

            if (!existHolidayDate.isEmpty()) {
                throw new BusinessException(CommonErrorCode.EXIST_HOLIDAY_DATE, holidayDTO.getHolidayDate());
            }

            ComCfgSysHolidayDate holidayDate = mapper.toEntity(holidayDTO);
            holidayDate.setRecordStatus(Constant.APPROVAL);
            result.add(repository.save(holidayDate));

            workingDateRepository.findByOrgCodeAndWorkDate(holidayDTO.getOrgCode(), holidayDate.getHolidayDate())
                    .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_EXIST_WORKING_DATE, holidayDate.getHolidayDate()));

            String orgCode = holidayDTO.getOrgCode();
            String holidayType = holidayDTO.getHolidayType();
            String holidayName = holidayDTO.getHolidayName();
            workingDateRepository.updateWorkingDate(orgCode, holidayDate.getHolidayDate(), holidayType, holidayName);
        }

        return result.stream().map(mapper::toDto).toList();
    }

    @Override
    public boolean existHolidayDate(LocalDate holidayDate) {
        return holidayRepository.findByHolidayDate(holidayDate).isPresent();
    }

    @Override
    public Page<HolidayResDTO> exportData(HolidaySearch search, Pageable pageable) {
        String keyword = search.getKeyword();
        String orgCode = search.getOrgCode();
        Integer year = parseYear(search.getYear());
        String holidayType = search.getHolidayType();

        return holidayRepository.getDataExportAll(keyword, orgCode, year, holidayType, pageable);
    }

    @Transactional
    @Override
    public HolidayDTO update(Long id, HolidayDTO dto) {
        HolidayDTO updateResult = super.update(id, dto);

        LocalDate holidayDate = dto.getHolidayDate();

        if (!existHolidayDate(holidayDate)) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_HOLIDAY_DATE, holidayDate);
        }

        workingDateRepository.findByOrgCodeAndWorkDate(dto.getOrgCode(), holidayDate)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_EXIST_WORKING_DATE, holidayDate));

        String orgCode = dto.getOrgCode();
        String holidayType = dto.getHolidayType();
        String holidayName = dto.getHolidayName();
        workingDateRepository.updateWorkingDate(orgCode, holidayDate, holidayType, holidayName);

        return updateResult;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        ComCfgSysHolidayDate res = holidayRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
        String orgCode = res.getOrgCode();
        LocalDate holidayDate = res.getHolidayDate();
        String holidayName = res.getHolidayName();
        ComCfgSysWorkingDate workingDate = workingDateRepository.findByOrgCodeAndWorkDate(orgCode, holidayDate)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_EXIST_WORKING_DATE, holidayDate));
        if (workingDate.getWorkingRegisterType() != null) {
            throw new BusinessException(CommonErrorCode.NOT_HOLIDAY_DATE, workingDate.getWorkingRegisterType());
        } else {
            int baseIsWorkingDay = workingDate.getBaseIsWorkingDay();
            super.delete(id);
            String startTime = findTimeByShiftPriority(holidayDate.getDayOfWeek().getValue(), Constant.getStartPriority(), true);
            String endTime = findTimeByShiftPriority(holidayDate.getDayOfWeek().getValue(), Constant.getEndPriority(), false);
            if (baseIsWorkingDay == 1) {
                workingDateRepository.updateWorkingDateWithNormalDay(orgCode, holidayDate, startTime, endTime);
            } else if (baseIsWorkingDay == 0) {
                workingDateRepository.updateWorkingDatWithHolidayDay(orgCode, holidayDate, holidayName);
            }

        }
    }

    private String findTimeByShiftPriority(int dayOfWeek, List<String> priorities, boolean wantStart) {
        for (String shift : priorities) {
            Optional<ComCfgSysWorkingHour> opt = workingHourRepository.findFirstByDayOfWeekAndShiftCode(dayOfWeek, shift);
            if (opt.isPresent()) {
                return wantStart ? opt.get().getStartTime() : opt.get().getEndTime();
            }
        }
        Optional<ComCfgSysWorkingHour> fallback = workingHourRepository.findFirstByDayOfWeek(dayOfWeek);
        return fallback.map(hour -> wantStart ? hour.getStartTime() : hour.getEndTime()).orElse(null);
    }

    private Integer parseYear(String year) {
        if (year == null) {
            return null;
        }
        String trimmed = year.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        for (char c : trimmed.toCharArray()) {
            if (!Character.isDigit(c)) {
                return null;
            }
        }
        return Integer.valueOf(trimmed);
    }
}
