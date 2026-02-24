package com.naas.admin_service.features.working_date_hour.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourDto;
import com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourExcel;
import com.naas.admin_service.features.working_date_hour.mapper.ComCfgSysWorkingHourMapper;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingHour;
import com.naas.admin_service.features.working_date_hour.repository.ComCfgSysWorkingHourRepository;
import com.naas.admin_service.features.working_date_hour.service.ComCfgSysWorkingHourService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComCfgSysWorkingHourServiceImpl extends BaseServiceImpl<ComCfgSysWorkingHour, SysWorkingHourDto> implements ComCfgSysWorkingHourService {
    private final ComCfgSysWorkingHourRepository repo;

    public ComCfgSysWorkingHourServiceImpl(
            ComCfgSysWorkingHourRepository repository,
            ComCfgSysWorkingHourMapper mapper
    ) {
        super(repository, mapper);
        this.repo = repository;
    }

    @Override
    public Page<SysWorkingHourDto> search(String keywords, Pageable pageable) {
        if (keywords != null && keywords.isBlank()) {
            keywords = null;
        }
        return repo.search(keywords, pageable);
    }

    @Override
    public List<ComCfgSysWorkingHour> create(@Valid List<SysWorkingHourDto> req) {
        if (req.isEmpty()) {
            throw new BusinessException(CommonErrorCode.INVALID_WORKING_HOUR);
        }

        List<ComCfgSysWorkingHour> result = new ArrayList<>();
        for (SysWorkingHourDto item : req) {
            boolean exists = repo.existsByDayOfWeekAndShiftCode(
                    item.getDayOfWeek(),
                    item.getShiftCode()
            );
            if (exists) {
                throw new BusinessException(CommonErrorCode.VALID_HOUR);
            }
            if (item.getIsWorkingDay() == 1) {
                validateTime(item.getStartTime());
                validateTime(item.getEndTime());
                validateTime(item.getBreakStartTime());
                validateTime(item.getBreakEndTime());
                validateDayOfWeek(item.getDayOfWeek());
            }
            ComCfgSysWorkingHour entity = mapper.toEntity(item);
            entity.setRecordStatus(Constant.APPROVAL);
            repo.save(entity);
        }
        return result;
    }

    @Override
    public List<SysWorkingHourExcel> exportData(String keyword, Pageable pageable) {
        Page<SysWorkingHourDto> workingHours = search(keyword, pageable);
        List<SysWorkingHourExcel> result = new ArrayList<>();
        for (SysWorkingHourDto item : workingHours.getContent()) {
            SysWorkingHourExcel excel = new SysWorkingHourExcel();
            excel.setOrgCode(item.getOrgCode());
            excel.setDayOfWeek(item.getDayOfWeek());
            excel.setShiftCode(item.getShiftCode());
            if (item.getIsWorkingDay() == 1) {
                excel.setIsWorkingDay("Có");
            }
            if (item.getIsWorkingDay() == 0) {
                excel.setIsWorkingDay("Không");
            }
            excel.setWorkingType(item.getWorkingType());
            excel.setWorkingTime(item.getStartTime() + " - " + item.getEndTime());
            excel.setEffectiveDate(item.getEffectiveDate());
            result.add(excel);
        }
        return result;
    }

    private void validateDayOfWeek(Integer day) {
        if (day == null || day < 2 || day > 8)
            throw new BusinessException(CommonErrorCode.ERROR_DAY_OF_WEEK);
    }

    private void validateTime(String time) {
        if (time == null || time.isBlank()) {
            return;
        }
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.ERROR_DATE_TIME);
        }
    }

}
