package com.naas.admin_service.features.log.service.impl;

import com.naas.admin_service.features.log.dto.LogSearchRequestDto;
import com.naas.admin_service.features.log.dto.ComInfLogAuditDto;
import com.naas.admin_service.features.log.dto.LogAuditSearchParams;
import com.naas.admin_service.features.log.model.ComInfLogAudit;
import com.naas.admin_service.features.log.repository.ComInfLogAuditRepository;
import com.naas.admin_service.features.log.service.ComInfLogAuditService;
import com.ngvgroup.bpm.core.logging.audit.dto.AuditFieldChange;
import com.ngvgroup.bpm.core.logging.audit.dto.AuditLogMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComInfLogAuditServiceImpl implements ComInfLogAuditService {
    private final ComInfLogAuditRepository logAuditRepository;
    @Override
    @Transactional
    public void save(AuditLogMessage msg) {
        if (msg.getChanges() == null || msg.getChanges().isEmpty()) {
            return;
        }

        for (AuditFieldChange fc : msg.getChanges()) {
            ComInfLogAudit e = new ComInfLogAudit();
            e.setRequestId(msg.getRequestId());
            e.setTableName(msg.getTableName());
            e.setRecordId(msg.getRecordId());
            e.setFieldName(fc.getFieldName());
            e.setOldValue(fc.getOldValue());
            e.setNewValue(fc.getNewValue());

            logAuditRepository.save(e);
        }
    }

// ...

    @Override
    public Page<ComInfLogAuditDto> search(LogSearchRequestDto requestDto, String keyword, Pageable pageable) {
        
        // 1. Xử lý logic cộng 1 ngày với java.sql.Date
        Date sqlToDatePlusOne = null;
        
        if (requestDto.getToDate() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(requestDto.getToDate()); // Set thời gian từ request (java.util.Date hoặc java.sql.Date đều được)
            c.add(Calendar.DAY_OF_MONTH, 1);   // Cộng 1 ngày
            
            // QUAN TRỌNG: Tạo java.sql.Date từ milliseconds
            sqlToDatePlusOne = new Date(c.getTimeInMillis());
        }

        // 2. Build param
        // Lưu ý: fromDate cũng nên ép kiểu về java.sql.Date nếu cần đồng bộ
        Date sqlFromDate = requestDto.getFromDate() != null ? new Date(requestDto.getFromDate().getTime()) : null;

        LogAuditSearchParams searchParams = LogAuditSearchParams.builder()
                .fromDate(sqlFromDate) 
                .toDate(sqlToDatePlusOne) // Truyền java.sql.Date vào đây
                .browserInfo(requestDto.getBrowserInfo())
                .username(requestDto.getUsername())
                .clientIp(requestDto.getClientIp())
                .tableName(requestDto.getTableName())
                .keyword(keyword)
                .build();

        return logAuditRepository.search(searchParams, pageable);
    }

    @Override
    @Transactional
    public int deleteActivityLogsByRequestIds(List<String> requestIds) {
        return logAuditRepository.deleteByRequestIdIn(requestIds);
    }

    @Override
    public List<ComInfLogAuditDto> getAll(LogSearchRequestDto requestDto, String keyword) {
        return logAuditRepository.getAll(requestDto.getFromDate(),
                requestDto.getToDate(),
                requestDto.getBrowserInfo(),
                requestDto.getUsername(),
                requestDto.getClientIp(),
                requestDto.getTableName(), keyword);
    }

    @Override
    public Page<ComInfLogAuditDto> getDetails(Long id, String keyword, Pageable pageable) {
        ComInfLogAudit audit = logAuditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit log not found with id: " + id));
        return logAuditRepository.getAllByRequestIdAndTableName(audit.getRequestId(), audit.getTableName(), keyword, pageable);
    }
}
