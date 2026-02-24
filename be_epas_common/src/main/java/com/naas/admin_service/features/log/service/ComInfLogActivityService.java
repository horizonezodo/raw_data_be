package com.naas.admin_service.features.log.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.naas.admin_service.features.log.dto.ComInfLogActivityDto;
import com.naas.admin_service.features.log.dto.LogSearchRequestDto;
import com.ngvgroup.bpm.core.logging.activity.dto.ActivityLogMessage;

import java.util.Date;
import java.util.List;

public interface ComInfLogActivityService {
    Page<ComInfLogActivityDto> search(LogSearchRequestDto requestDto, String keyword, Pageable pageable);
    void save(ActivityLogMessage msg);
    List<String> deleteActivityLogsBefore(Date cutoff);
    List<ComInfLogActivityDto> getAll(LogSearchRequestDto requestDto, String keyword);
}
