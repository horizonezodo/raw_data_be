package com.naas.admin_service.features.working_date_hour.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.working_date_hour.dto.holiday.DayWorkInfo;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysHolidayDate;
import com.naas.admin_service.features.working_date_hour.repository.HolidayRepository;
import com.naas.admin_service.features.working_date_hour.dto.working_date.ComCfgSysWorkingDateDto;
import com.naas.admin_service.features.working_date_hour.dto.working_date.WorkingDateResDto;
import com.naas.admin_service.features.working_date_hour.dto.working_date.WorkingDateSearchRequest;
import com.naas.admin_service.features.working_date_hour.dto.working_date.YearInitDto;
import com.naas.admin_service.features.working_date_hour.mapper.WorkingDateMapper;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingDate;
import com.naas.admin_service.features.working_date_hour.repository.ComCfgSysWorkingDateRepository;
import com.naas.admin_service.features.working_date_hour.service.ComCfgSysWorkingDateService;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingHour;
import com.naas.admin_service.features.working_date_hour.repository.ComCfgSysWorkingHourRepository;
import com.naas.admin_service.features.common.repository.CtgComCommonRepository;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComCfgSysWorkingDateServiceImpl extends BaseServiceImpl<ComCfgSysWorkingDate, WorkingDateResDto> implements ComCfgSysWorkingDateService {

    private final ComCfgSysWorkingDateRepository comCfgSysWorkingDateRepository;
    private final CtgComCommonRepository comCfgCommonRepository;
    private final ComCfgSysWorkingHourRepository comCfgSysWorkingHourRepository;
    private final HolidayRepository holidayRepository;

    public ComCfgSysWorkingDateServiceImpl(ComCfgSysWorkingDateRepository comCfgSysWorkingDateRepository,
                                           CtgComCommonRepository comCfgCommonRepository,
                                           ComCfgSysWorkingHourRepository comCfgSysWorkingHourRepository,
                                           HolidayRepository holidayRepository,
                                           WorkingDateMapper workingDateMapper) {
        super(comCfgSysWorkingDateRepository, workingDateMapper);
        this.comCfgSysWorkingDateRepository = comCfgSysWorkingDateRepository;
        this.comCfgCommonRepository = comCfgCommonRepository;
        this.comCfgSysWorkingHourRepository = comCfgSysWorkingHourRepository;
        this.holidayRepository = holidayRepository;
    }

    @Override
    public List<WorkingDateResDto> searchWorkingDate(WorkingDateSearchRequest request) {
        int year = request.getYear();
        String orgCode = request.getOrgCode();

        String monthStr = request.getMonth();

        LocalDate start;
        LocalDate end;

        if ("all".equalsIgnoreCase(monthStr)) {
            start = LocalDate.of(year, 1, 1);
            end = LocalDate.of(year, 12, 31);
        } else {
            int month = Integer.parseInt(monthStr);
            start = LocalDate.of(year, month, 1);
            end = start.withDayOfMonth(start.lengthOfMonth());
        }

        List<ComCfgSysWorkingDate> workingDates = comCfgSysWorkingDateRepository
                .findByOrgCodeAndWorkDateBetween(orgCode, start, end);

        List<ComCfgSysHolidayDate> holidays = holidayRepository
                .findByOrgCodeAndHolidayDateBetween(orgCode, start, end);

        Map<LocalDate, ComCfgSysHolidayDate> holidayMap = holidays.stream()
                .collect(Collectors.toMap(ComCfgSysHolidayDate::getHolidayDate, h -> h));

        return workingDates.stream().map(w -> {
            WorkingDateResDto dto = new WorkingDateResDto();
            dto.setOrgCode(w.getOrgCode());
            dto.setWorkDate(w.getWorkDate());
            dto.setBaseIsWorkingDay(w.getBaseIsWorkingDay());
            dto.setIsWorkingDay(w.getIsWorkingDay());
            dto.setWorkingRegisterType(w.getWorkingRegisterType());
            dto.setStartTime(w.getStartTime());
            dto.setEndTime(w.getEndTime());
            dto.setIsGenerated(w.getIsGenerated());
            dto.setReason(w.getDescription());

            ComCfgSysHolidayDate h = holidayMap.get(w.getWorkDate());
            if (h != null) {
                dto.setHolidayType(h.getHolidayType());
                dto.setHolidayName(h.getHolidayName());
            } else {
                dto.setHolidayType(w.getHolidayType());
                dto.setHolidayName(w.getHolidayName());
            }
            return dto;
        }).toList();
    }

    @Override
    public void registerWorkingDate(ComCfgSysWorkingDateDto request) {

        LocalDate today = LocalDate.now();

        ComCfgSysWorkingDate comCfgSysWorkingDate =
                comCfgSysWorkingDateRepository.findByOrgCodeAndWorkDate(
                        request.getOrgCode(), request.getWorkDate()
                ).orElseThrow(() -> new BusinessException(CommonErrorCode.INVALID_WORKING_DATE, request.getWorkDate()));

        if (!request.getWorkDate().isAfter(today)) {
            throw new BusinessException(CommonErrorCode.SMALLER_WORKING_DATE_REGISTER, request.getWorkDate());
        }

        if (request.getWorkingRegisterType() == null ||
                request.getIsWorkingDay() == null || request.getIsWorkingDay() != 1 ||
                request.getIsGenerated() == null || request.getIsGenerated() != 0 ||
                request.getStartTime() == null ||
                request.getEndTime() == null) {
            throw new BusinessException(CommonErrorCode.INVALID_WORKING_DATE_REGISTER, request.getWorkDate());
        }

        ComCfgSysWorkingDate entity = getComCfgSysWorkingDate(request, comCfgSysWorkingDate);
        comCfgSysWorkingDateRepository.save(entity);
    }

    @NotNull
    private static ComCfgSysWorkingDate getComCfgSysWorkingDate(ComCfgSysWorkingDateDto request, ComCfgSysWorkingDate comCfgSysWorkingDate) {
        comCfgSysWorkingDate.setOrgCode(request.getOrgCode());
        comCfgSysWorkingDate.setWorkDate(request.getWorkDate());
        comCfgSysWorkingDate.setWorkingRegisterType(request.getWorkingRegisterType());
        comCfgSysWorkingDate.setIsWorkingDay(1);
        comCfgSysWorkingDate.setIsGenerated(0);
        comCfgSysWorkingDate.setStartTime(request.getStartTime());
        comCfgSysWorkingDate.setEndTime(request.getEndTime());

        comCfgSysWorkingDate.setIsDelete(0);
        comCfgSysWorkingDate.setCreatedBy(Constant.SYSTEM);
        comCfgSysWorkingDate.setApprovedBy(Constant.SYSTEM);
        comCfgSysWorkingDate.setModifiedBy(Constant.SYSTEM);
        comCfgSysWorkingDate.setRecordStatus(Constant.APPROVAL);
        comCfgSysWorkingDate.setDescription(request.getReason());
        return comCfgSysWorkingDate;
    }

    @Override
    public void removeWorkingDate(ComCfgSysWorkingDateDto request) {

        ComCfgSysWorkingDate existing =
                comCfgSysWorkingDateRepository.findByOrgCodeAndWorkDate(
                        request.getOrgCode(), request.getWorkDate()
                ).orElseThrow(() -> new BusinessException(CommonErrorCode.WORKING_DATE_NOT_FOUND,
                        request.getWorkDate()));

        existing.setWorkingRegisterType(null);
        existing.setIsWorkingDay(0);
        existing.setIsGenerated(0);
        existing.setStartTime(null);
        existing.setEndTime(null);

        comCfgSysWorkingDateRepository.save(existing);
    }

    @Override
    @Transactional
    public void inItWorkingYear(YearInitDto request) {
        validateInitRequest(request);

        String orgCode = request.getOrgCode();
        int year = request.getYear();
        boolean includeSat = request.isSat();
        boolean includeSun = request.isSun();

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        comCfgSysWorkingDateRepository.deleteByOrgCodeAndWorkDateBetween(orgCode, start, end);

        Map<LocalDate, ComCfgSysHolidayDate> holidayMap = holidayRepository
                .findByOrgCodeAndHolidayDateBetween(orgCode, start, end)
                .stream()
                .collect(Collectors.toMap(ComCfgSysHolidayDate::getHolidayDate, h -> h));

        int estimatedSize = (int) ChronoUnit.DAYS.between(start, end) + 1;
        List<ComCfgSysWorkingDate> inserts = new ArrayList<>(estimatedSize);

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            int dow = date.getDayOfWeek().getValue();

            DayWorkInfo baseDayInfo = determineBaseWorkingDay(dow, includeSat, includeSun);

            if (holidayMap.containsKey(date)) {
                ComCfgSysHolidayDate h = holidayMap.get(date);

                if ((dow == 6 && includeSat) || (dow == 7 && includeSun) || (dow >= 1 && dow <= 5)) {
                    baseDayInfo = new DayWorkInfo(
                            0,
                            h.getHolidayType(),
                            h.getHolidayName()
                    );
                }
            }

            String startTime = null;
            String endTime = null;

            if (baseDayInfo.getBaseIsWorkingDay() == 1) {
                startTime = findTimeByShiftPriority(dow, Constant.getStartPriority(), true);
                endTime = findTimeByShiftPriority(dow, Constant.getEndPriority(), false);
            }

            inserts.add(ComCfgSysWorkingDate.buildEntity(orgCode, date, baseDayInfo, startTime, endTime));
        }

        comCfgSysWorkingDateRepository.saveAll(inserts);
    }

    private String findTimeByShiftPriority(int dayOfWeek, List<String> priorities, boolean wantStart) {
        for (String shift : priorities) {
            Optional<ComCfgSysWorkingHour> opt = comCfgSysWorkingHourRepository.findFirstByDayOfWeekAndShiftCode(dayOfWeek, shift);
            if (opt.isPresent()) {
                return wantStart ? opt.get().getStartTime() : opt.get().getEndTime();
            }
        }
        Optional<ComCfgSysWorkingHour> fallback = comCfgSysWorkingHourRepository.findFirstByDayOfWeek(dayOfWeek);
        return fallback.map(hour -> wantStart ? hour.getStartTime() : hour.getEndTime()).orElse(null);
    }

    @Override
    public WorkingDateResDto create(WorkingDateResDto dto) {
        return null;
    }

    @Override
    public WorkingDateResDto update(Long aLong, WorkingDateResDto dto) {
        return null;
    }

    @Override
    public WorkingDateResDto findById(Long aLong) {
        return null;
    }

    @Override
    public List<WorkingDateResDto> findAll() {
        return List.of();
    }

    private DayWorkInfo determineBaseWorkingDay(int dow, boolean includeSat, boolean includeSun) {
        int working = 1;
        String holidayType = null;
        String holidayName = null;

        if (dow == 6 && includeSat) {
            working = 0;
            holidayType = Constant.WEEKEND;
            holidayName = findHolidayNameByCode();
        }

        if (dow == 7 && includeSun) {
            working = 0;
            holidayType = Constant.WEEKEND;
            holidayName = findHolidayNameByCode();
        }

        return new DayWorkInfo(working, holidayType, holidayName);
    }

    private String findHolidayNameByCode() {
        return comCfgCommonRepository.findCommonNameByCode(Constant.WEEKEND).orElse(null);
    }

    private void validateInitRequest(YearInitDto request) {
        if (request == null || request.getOrgCode() == null || request.getYear() <= 0) {
            throw new BusinessException(CommonErrorCode.INVALID_INIT_YEAR_REQUEST, request);
        }
    }

}
