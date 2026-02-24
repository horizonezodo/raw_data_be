package com.naas.admin_service.features.mail.service.impl;

import com.naas.admin_service.core.contants.EntityStatus;
import com.naas.admin_service.core.utils.PageUtils;
import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateRequest;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse;
import com.naas.admin_service.features.mail.mapper.ComCfgMailTemplateMapper;
import com.naas.admin_service.features.mail.model.ComCfgMailTemplate;
import com.naas.admin_service.features.mail.repository.ComCfgMailTemplateRepository;
import com.naas.admin_service.features.mail.service.ComCfgMailTemplateService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ComCfgMailTemplateServiceImpl implements ComCfgMailTemplateService {
    private final ComCfgMailTemplateRepository comCfgMailTemplateRepository;
    private final ComCfgMailTemplateMapper comCfgMailTemplateMapper;
    private final ExcelService excelService;

    public ComCfgMailTemplateServiceImpl(ComCfgMailTemplateRepository comCfgMailTemplateRepository,
            ComCfgMailTemplateMapper comCfgMailTemplateMapper, ExcelService excelService) {
        this.comCfgMailTemplateRepository = comCfgMailTemplateRepository;
        this.comCfgMailTemplateMapper = comCfgMailTemplateMapper;
        this.excelService = excelService;
    }

    @Override
    public PageResponse<ComCfgMailTemplateResponse> getAll(SearchFilterRequest filterRequest) {

        Pageable pageable = PageUtils.toPageable(filterRequest.getPageable());
        Page<ComCfgMailTemplateResponse> comCfgMailResponses = comCfgMailTemplateRepository
                .getAll(filterRequest.getFilter(), pageable);

        return new PageResponse<>(comCfgMailResponses);
    }

    @Override
    public ComCfgMailTemplateResponse getDetail(String mailTemplateCode) {
        return comCfgMailTemplateRepository.getDetail(mailTemplateCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND ,
                        mailTemplateCode));
    }

    @Override
    @Transactional
    public void createComCfgMailTemplate(ComCfgMailTemplateRequest comCfgMailTemplateRequest) {

        boolean exists = comCfgMailTemplateRepository
                .existsByMailTemplateCodeAndIsDelete(comCfgMailTemplateRequest.getMailTemplateCode(),
                        EntityStatus.IsDelete.NOT_DELETED.getValue());
        if (exists) {
            throw new BusinessException(ErrorCode.CONFLICT,
                    comCfgMailTemplateRequest.getMailTemplateCode());
        }

        ComCfgMailTemplate comCfgMailTemplate = comCfgMailTemplateMapper.toEntity(comCfgMailTemplateRequest);
        comCfgMailTemplateRepository.save(comCfgMailTemplate);
    }

    @Override
    @Transactional
    public void updateComCfgMail(String mailTemplateCode, ComCfgMailTemplateRequest comCfgMailTemplateRequest) {

        ComCfgMailTemplate comCfgMailTemplate = comCfgMailTemplateRepository.findByMailTemplateCode(mailTemplateCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                        mailTemplateCode));

        comCfgMailTemplateMapper.updateMailTemplateFromDto(comCfgMailTemplateRequest, comCfgMailTemplate);
        comCfgMailTemplate.setMailTemplateCode(mailTemplateCode);
        comCfgMailTemplateRepository.save(comCfgMailTemplate);
    }

    @Override
    @Transactional
    public void deleteComCfgMail(String mailTemplateCode) {
        ComCfgMailTemplate comCfgMailTemplate = comCfgMailTemplateRepository.findByMailTemplateCode(mailTemplateCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                        mailTemplateCode));

        comCfgMailTemplate.setIsDelete(EntityStatus.IsDelete.DELETED.getValue());
        comCfgMailTemplate.setIsActive(EntityStatus.IsActive.NOT_ACTIVE.getValue());
        comCfgMailTemplate.setRecordStatus(EntityStatus.RecordStatus.CANCEL.getValue());
        comCfgMailTemplateRepository.save(comCfgMailTemplate);
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request) {
        List<ComCfgMailTemplateResponse> res = comCfgMailTemplateRepository.getAllList(request.getFilter());
        byte[] response = excelService.exportToExcelContent(res, request.getLabels(), ComCfgMailTemplateResponse.class);
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(response);
        } catch (Exception e) {
            log.error("Error: Lỗi tạo file Excel {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
