package com.naas.admin_service.features.mail.service.impl;

import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.core.utils.AesEncryptionUtil;
import com.naas.admin_service.core.utils.PageUtils;
import com.naas.admin_service.features.mail.mapper.ComCfgMailFromMapper;
import com.naas.admin_service.features.mail.model.ComCfgMailFrom;
import com.naas.admin_service.features.mail.repository.ComCfgMailFromRepository;
import com.naas.admin_service.features.mail.service.ComCfgMailFromService;
import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromDto;
import com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromResponse;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse;
import com.naas.admin_service.features.mail.mapper.ComCfgMailTemplateMapper;
import com.naas.admin_service.features.mail.model.ComCfgMailTemplate;
import com.naas.admin_service.features.mail.repository.ComCfgMailTemplateRepository;
import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ComCfgMailFromServiceImpl extends BaseStoredProcedureService implements ComCfgMailFromService {
    private static final String MAIL_CODE_PATTERN = "%s.%03d";

    private final ComCfgMailFromRepository comCfgMailFromRepository;
    private final ComCfgMailFromMapper comCfgMailFromMapper;
    private final ComCfgMailTemplateRepository comCfgMailTemplateRepository;
    private final AesEncryptionUtil aesEncryptionUtil;
    private final ExcelService excelService;
    private final ComCfgMailTemplateMapper comCfgMailTemplateMapper;


    public ComCfgMailFromServiceImpl(ComCfgMailFromRepository comCfgMailFromRepository,
                                     ComCfgMailFromMapper comCfgMailFromMapper,
                                     ComCfgMailTemplateRepository comCfgMailTemplateRepository,
                                     AesEncryptionUtil aesEncryptionUtil,
                                     ExcelService excelService,
                                     ComCfgMailTemplateMapper comCfgMailTemplateMapper
    ) {
        super();
        this.comCfgMailFromRepository = comCfgMailFromRepository;
        this.comCfgMailFromMapper = comCfgMailFromMapper;
        this.comCfgMailTemplateRepository = comCfgMailTemplateRepository;
        this.aesEncryptionUtil = aesEncryptionUtil;
        this.excelService = excelService;
        this.comCfgMailTemplateMapper = comCfgMailTemplateMapper;
    }

    @Override
    @Transactional
    public void createComCfgMailFrom(ComCfgMailFromDto comCfgMailFromDto) {

        LocalDateTime localDate = LocalDate.now().atStartOfDay();
        LocalDateTime endDate = localDate.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DATE_PATTERN);
        String date = localDate.format(formatter);

        long mailFromQuantity = comCfgMailFromRepository.countByCreatedDateBetween(localDate, endDate);
        mailFromQuantity++;
        String mailCode = String.format(MAIL_CODE_PATTERN, date, mailFromQuantity);

        ComCfgMailFrom comCfgMailFrom = comCfgMailFromMapper.toEntity(comCfgMailFromDto);
        comCfgMailFrom.setMailCode(mailCode);
      

        // encode password
        if (Boolean.TRUE.equals(comCfgMailFromDto.getIsEncrypted())) {
            String password = aesEncryptionUtil.encrypt(comCfgMailFromDto.getMailPassword());
            comCfgMailFrom.setMailPassword(password);
        }

        if (comCfgMailFromDto.getTemplateIds() != null
                && !comCfgMailFromDto.getTemplateIds().isEmpty()) {

            List<ComCfgMailTemplate> comCfgMailTemplates = comCfgMailTemplateRepository
                    .findComCfgMailTemplatesByIdIs(comCfgMailFromDto.getTemplateIds());

            if (comCfgMailTemplates.size() != comCfgMailFromDto.getTemplateIds().size()) {
                List<Long> templateIds = comCfgMailFromDto.getTemplateIds();
                List<Long> notFoundTemplateIds = comCfgMailTemplates.stream()
                        .map(ComCfgMailTemplate::getId)
                        .filter(id -> !templateIds.contains(id)).toList();
                throw new BusinessException(ErrorCode.NOT_FOUND,
                        "template ids: " + notFoundTemplateIds);
            }

            List<Long> comCfgMailTemplateIds = comCfgMailTemplates.stream()
                    .filter(item -> item.getMailCode() != null)
                    .map(ComCfgMailTemplate::getId).toList();

            if (!comCfgMailTemplateIds.isEmpty()) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "template mail đã được sử dụng " + comCfgMailTemplateIds);
            }

            comCfgMailTemplates.forEach(comCfgMailTemplate -> comCfgMailTemplate.setMailCode(mailCode));

            comCfgMailTemplateRepository.saveAll(comCfgMailTemplates);
        }

        comCfgMailFromRepository.save(comCfgMailFrom);
    }

    @Override
    @Transactional
    public void updateComCfgMailFrom(Long id, ComCfgMailFromDto comCfgMailFromDto) {
        ComCfgMailFrom comCfgMailFrom = comCfgMailFromRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.EMAIL_CONFIG_NOT_FOUND, id));

        String mailCode = comCfgMailFrom.getMailCode();

        List<ComCfgMailTemplate> oldComCfgMailTemplate = comCfgMailTemplateRepository
                .findComCfgMailTemplatesByMailCode(mailCode);

        updatePasswordIfChanged(comCfgMailFromDto, comCfgMailFrom);
        List<Long> templateIdsRequestUpdate = comCfgMailFromDto.getTemplateIds();
        List<ComCfgMailTemplate> listComCfgMailTemplateUpdate = prepareTemplateUpdates(templateIdsRequestUpdate, mailCode, oldComCfgMailTemplate);

        if (!listComCfgMailTemplateUpdate.isEmpty()) {
            comCfgMailTemplateRepository.saveAll(listComCfgMailTemplateUpdate);
        }

        comCfgMailFromMapper.updateMailTemplateFromDto(comCfgMailFromDto, comCfgMailFrom);
        comCfgMailFromRepository.save(comCfgMailFrom);
    }

    @Override
    public ComCfgMailFromDto findComCfgMailFromById(Long id) {

        ComCfgMailFrom comCfgMailFrom =  comCfgMailFromRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.EMAIL_CONFIG_NOT_FOUND, + id));

        ComCfgMailFromDto comCfgMailFromDto = comCfgMailFromMapper.toDto(comCfgMailFrom);

        List<ComCfgMailTemplateResponse> comCfgMailTemplates = comCfgMailTemplateRepository
                .findComCfgMailTemplatesByMailCode(comCfgMailFrom.getMailCode())
                .stream().map(comCfgMailTemplateMapper::toResponse)
                .toList();

        comCfgMailFromDto.setTemplateResponses(comCfgMailTemplates);
        return comCfgMailFromDto;
    }

    private void updatePasswordIfChanged(ComCfgMailFromDto comCfgMailFromDto, ComCfgMailFrom comCfgMailFrom) {
        boolean isEncryptionChanged = !comCfgMailFrom.getIsEncrypted().equals(comCfgMailFromDto.getIsEncrypted());
        boolean isPasswordChanged = !comCfgMailFrom.getMailPassword().equals(comCfgMailFromDto.getMailPassword());
        if (!isEncryptionChanged && !isPasswordChanged) {
            return;
        }

        String newPassword;
        if (Boolean.TRUE.equals(comCfgMailFromDto.getIsEncrypted())) {
            newPassword = aesEncryptionUtil.encrypt(comCfgMailFromDto.getMailPassword());
        } else if (comCfgMailFrom.getMailPassword().equals(comCfgMailFromDto.getMailPassword())) {
            newPassword = aesEncryptionUtil.decrypt(comCfgMailFromDto.getMailPassword());
        } else {
            newPassword = comCfgMailFromDto.getMailPassword();
        }
        comCfgMailFrom.setIsEncrypted(comCfgMailFromDto.getIsEncrypted());
        comCfgMailFrom.setMailPassword(newPassword);
    }

    private List<ComCfgMailTemplate> prepareTemplateUpdates(List<Long> templateIdsRequestUpdate, String mailCode, List<ComCfgMailTemplate> oldComCfgMailTemplate) {
        List<ComCfgMailTemplate> listComCfgMailTemplateUpdate = new ArrayList<>();
        if (templateIdsRequestUpdate == null || templateIdsRequestUpdate.isEmpty()) {
            if (!oldComCfgMailTemplate.isEmpty()) {
                oldComCfgMailTemplate.forEach(item -> item.setMailCode(null));
                listComCfgMailTemplateUpdate.addAll(oldComCfgMailTemplate);
            }
            return listComCfgMailTemplateUpdate;
        }

        List<ComCfgMailTemplate> newComCfgMailTemplate = comCfgMailTemplateRepository
                .findComCfgMailTemplatesByIdIs(templateIdsRequestUpdate);

        Set<Long> foundTemplateIds = newComCfgMailTemplate.stream()
                .map(ComCfgMailTemplate::getId)
                .collect(Collectors.toSet());
        List<Long> notFoundTemplateIds = templateIdsRequestUpdate.stream()
                .filter(templateId -> !foundTemplateIds.contains(templateId))
                .toList();
        if (!notFoundTemplateIds.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND,
                    "template ids: " + notFoundTemplateIds);
        }

        newComCfgMailTemplate.stream()
                .filter(item -> !Objects.equals(item.getMailCode(), mailCode))
                .findAny()
                .ifPresent(item -> {
                    throw new BusinessException(CommonErrorCode.BAD_REQUEST, "templateIds already used: " + item.getMailCode());
                });

        List<ComCfgMailTemplate> mergedTemplates = new ArrayList<>(oldComCfgMailTemplate);
        mergedTemplates.addAll(newComCfgMailTemplate);
        Set<Long> templateIdRequests = new HashSet<>(templateIdsRequestUpdate);
        mergedTemplates.forEach(item -> {
            if (!templateIdRequests.contains(item.getId())) {
                item.setMailCode(null);
                listComCfgMailTemplateUpdate.add(item);
            } else if (item.getMailCode() == null) {
                item.setMailCode(mailCode);
                listComCfgMailTemplateUpdate.add(item);
            }
        });
        return listComCfgMailTemplateUpdate;
    }

    @Override
    public Page<ComCfgMailFromResponse> searchConCfgMailFrom(SearchFilterRequest searchFilterRequest) {
        Pageable pageable = PageUtils.toPageable(searchFilterRequest.getPageable());
        String filter = searchFilterRequest.getFilter() != null
                ? searchFilterRequest.getFilter().toLowerCase()
                : null;
        return comCfgMailFromRepository
                .searchComCfgMailFrom(filter, pageable);
    }

    @Override
    @Transactional
    public void deleteComCfgMailFromById(Long id) {
        ComCfgMailFrom comCfgMailFrom = comCfgMailFromRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.EMAIL_CONFIG_NOT_FOUND, id));

        comCfgMailFrom.setIsDelete(1);
        comCfgMailFrom.setIsActive(0);

        List<ComCfgMailTemplate> comCfgMailTemplate = comCfgMailTemplateRepository
                .findComCfgMailTemplatesByMailCode(comCfgMailFrom.getMailCode());

        comCfgMailTemplate.forEach(item -> item.setMailCode(null));

        comCfgMailTemplateRepository.saveAll(comCfgMailTemplate);
        comCfgMailFromRepository.save(comCfgMailFrom);
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request) {
        String filter = request.getFilter() != null
                ? request.getFilter().toLowerCase()
                : null;
        List<ComCfgMailFromResponse> res = comCfgMailFromRepository.searchComCfgMailFrom(filter);
        byte[] response = excelService.exportToExcelContent(res, request.getLabels(), ComCfgMailFromResponse.class);
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
