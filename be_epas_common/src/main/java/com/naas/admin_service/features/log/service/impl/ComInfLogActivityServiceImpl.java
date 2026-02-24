package com.naas.admin_service.features.log.service.impl;

import com.naas.admin_service.features.log.model.ComInfLogActivity;
import com.naas.admin_service.features.log.repository.ComInfLogActivityRepository;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.naas.admin_service.features.log.dto.ComInfLogActivityDto;
import com.naas.admin_service.features.log.dto.LogSearchRequestDto;
import com.naas.admin_service.features.log.service.ComInfLogActivityService;
import com.ngvgroup.bpm.core.logging.activity.dto.ActivityLogMessage;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComInfLogActivityServiceImpl implements ComInfLogActivityService {
    private final ComInfLogActivityRepository logActivityRepository;

    @Override
    public Page<ComInfLogActivityDto> search(LogSearchRequestDto requestDto, String keyword,
                                                     Pageable pageable) {
        return logActivityRepository.search(requestDto.getFromDate(), 
                requestDto.getToDate(), 
                requestDto.getBrowserInfo(), 
                requestDto.getUsername(), 
                requestDto.getClientIp(), 
                keyword, pageable);
    }

    @Transactional
    @Override
    public void save(ActivityLogMessage msg) {
        ComInfLogActivity e = new ComInfLogActivity();
        e.setRequestId(msg.getRequestId());
        e.setEventTime(msg.getEventTime());
        e.setStatusCode(msg.getStatusCode());
        e.setUsername(msg.getUsername());
        e.setFunctionCode(msg.getFunctionCode());
        e.setActionName(msg.getActionName());
        e.setRequestUrl(msg.getRequestUrl());
        e.setMethodCode(msg.getMethodCode());
        e.setServiceName(msg.getServiceName());
        e.setClientIp(msg.getClientIp());
        e.setClientName(msg.getClientName());
        e.setBrowserInfo(msg.getBrowserInfo());
        e.setDurationTime(msg.getDurationTime());
        e.setRequestPayload(msg.getRequestPayload());

        logActivityRepository.save(e);
    }

    @Override
    @Transactional
    public List<String> deleteActivityLogsBefore(Date cutoff) {
        List<String> requestIds = logActivityRepository.findRequestIdByEventTimeBefore(cutoff);
        logActivityRepository.deleteByEventTimeBefore(cutoff);
        return requestIds;
    }

    @Override
    public List<ComInfLogActivityDto> getAll(LogSearchRequestDto requestDto, String keyword) {
        return logActivityRepository.getAll(requestDto.getFromDate(), 
                requestDto.getToDate(), 
                requestDto.getBrowserInfo(), 
                requestDto.getUsername(), 
                requestDto.getClientIp(), keyword);
    }
}
