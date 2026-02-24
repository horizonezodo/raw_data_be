package com.naas.admin_service.features.log.service;

import com.naas.admin_service.features.log.dto.LogSearchRequestDto;
import com.ngvgroup.bpm.core.logging.audit.dto.AuditLogMessage;
import com.naas.admin_service.features.log.dto.ComInfLogAuditDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ComInfLogAuditService {
    void save(AuditLogMessage msg);
    Page<ComInfLogAuditDto> search(LogSearchRequestDto requestDto, String keyword, Pageable pageable);

    int deleteActivityLogsByRequestIds(List<String> requestIds);
    List<ComInfLogAuditDto> getAll(LogSearchRequestDto requestDto, String keyword);
    Page<ComInfLogAuditDto> getDetails(Long id, String keyword, Pageable pageable);
}
