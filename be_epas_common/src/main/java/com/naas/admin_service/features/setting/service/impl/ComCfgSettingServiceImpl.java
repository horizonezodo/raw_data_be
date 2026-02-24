package com.naas.admin_service.features.setting.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.setting.dto.AppConfigResponseDto;
import com.naas.admin_service.features.tenant.service.TenantDbConfigService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naas.admin_service.features.setting.dto.ComCfgSettingReqDto;
import com.naas.admin_service.features.setting.dto.ReportDto;
import com.naas.admin_service.features.setting.mapper.ComCfgSettingMapper;
import com.naas.admin_service.features.setting.model.ComCfgSetting;
import com.naas.admin_service.features.setting.repository.ComCfgSettingRepository;
import com.naas.admin_service.features.setting.service.ComCfgSettingService;
import com.naas.admin_service.features.common.repository.CtgComCommonRepository;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.naas.admin_service.features.common.service.MinIOService;
import com.naas.admin_service.core.contants.SettingCode;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ComCfgSettingServiceImpl implements ComCfgSettingService {

    private final ComCfgSettingRepository comCfgSettingRepository;
    private final ComCfgSettingMapper comCfgSettingMapper;
    private final MinIOService minIOService;
    private final CtgComCommonRepository repo;
    private final EntityManager entityManager;
    private final TenantDbConfigService tenantDbConfigService;

    @Transactional
    public void createOrUpdateSettings(List<ComCfgSettingReqDto> requests) {
        for (ComCfgSettingReqDto req : requests) {
            validateSettingCode(req);
            String settingValue = normalizeSettingValue(req);
            req.setSettingValue(settingValue);

            comCfgSettingRepository.findBySettingCode(req.getSettingCode())
                    .ifPresentOrElse(existing -> updateExistingSetting(req, settingValue, existing),
                            () -> createNewSetting(req));
        }
    }

    private void validateSettingCode(ComCfgSettingReqDto req) {
        if (req.getSettingCode() == null || req.getSettingCode().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Setting code khong duoc de trong");
        }
    }

    private String normalizeSettingValue(ComCfgSettingReqDto req) {
        return req.getSettingValue() != null ? req.getSettingValue() : "";
    }

    private void updateExistingSetting(ComCfgSettingReqDto req, String settingValue, ComCfgSetting existing) {
        deleteOldImageIfNeeded(req.getSettingCode(), settingValue, existing.getSettingValue());
        comCfgSettingMapper.updateEntity(req, existing);
        comCfgSettingRepository.save(existing);
    }

    private void deleteOldImageIfNeeded(String settingCode, String newValue, String oldValue) {
        if (!isImageSetting(settingCode) || oldValue == null || oldValue.equals(newValue) || oldValue.trim().isEmpty()) {
            return;
        }
        String objectName = extractObjectNameFromSettingValue(oldValue);
        if (objectName != null) {
            try {
                minIOService.deleteFile(objectName);
            } catch (Exception e) {
                // Silent fail - continue with update
            }
        }
    }

    private boolean isImageSetting(String settingCode) {
        return SettingCode.LAYOUT.IMAGE_LOGIN.equals(settingCode) || SettingCode.LAYOUT.IMAGE_LEFT.equals(settingCode);
    }

    private void createNewSetting(ComCfgSettingReqDto req) {
        ComCfgSetting newSetting = comCfgSettingMapper.toEntity(req);

        newSetting.setRecordStatus("approval");
        newSetting.setIsDelete(0);
        newSetting.setIsActive(1);

        comCfgSettingRepository.save(newSetting);
    }

    private String extractObjectNameFromSettingValue(String settingValue) {
        if (settingValue == null || settingValue.trim().isEmpty()) {
            return null;
        }

        // If it's a full URL, extract the path after the domain
        if (settingValue.startsWith("http")) {
            // Try different patterns to extract object name

            // Pattern 1: /uploads/epas-public/path/to/file
            int index = settingValue.indexOf("/uploads/epas-public/");
            if (index != -1) {
                return settingValue.substring(index + "/uploads/epas-public/".length());
            }

            // Pattern 2: /asset/epas-public/path/to/file
            index = settingValue.indexOf("/asset/epas-public/");
            if (index != -1) {
                return settingValue.substring(index + "/asset/epas-public/".length());
            }

            // Pattern 3: /asset/path/to/file (without epas-public)
            index = settingValue.indexOf("/asset/");
            if (index != -1) {
                return settingValue.substring(index + "/asset/".length());
            }

            // Pattern 4: Extract filename from URL
            int lastSlash = settingValue.lastIndexOf('/');
            if (lastSlash != -1) {
                return settingValue.substring(lastSlash + 1);
            }
        }

        // If it's already a relative path, return as is
        return settingValue;
    }

    @Override
    public List<ComCfgSettingReqDto> getSettingsByCodes(List<String> settingCodes) {
        List<ComCfgSetting> settings = comCfgSettingRepository.findBySettingCodeIn(settingCodes);
        return comCfgSettingMapper.toDtoList(settings);
    }

    @Override
    public List<ComCfgSettingReqDto> getLayoutPublicSettings() {
        List<String> layoutSettingCodes = Arrays.asList(
                SettingCode.LAYOUT.SYSTEM_NAME,
                SettingCode.LAYOUT.PHONE,
                SettingCode.LAYOUT.MAIL,
                SettingCode.LAYOUT.WEBSITE,
                SettingCode.LAYOUT.IMAGE_LOGIN,
                SettingCode.LAYOUT.IMAGE_LEFT,
                SettingCode.LAYOUT.SSO,
                SettingCode.LAYOUT.SESSION,
                SettingCode.LAYOUT.HEADER,
                SettingCode.LAYOUT.FOOTER,
                SettingCode.LAYOUT.IS_FULL_FOOTER);

        return getSettingsByCodes(layoutSettingCodes);
    }

    @Override
    public List<ComCfgSettingReqDto> getCaptchaSettings() {
        List<String> captchaSettingCodes = Arrays.asList(
                SettingCode.CAPTCHA.LENGTH,
                SettingCode.CAPTCHA.DATA_TYPE,
                SettingCode.CAPTCHA.CASE_SENSITIVE,
                SettingCode.CAPTCHA.IS_APPLY);
        return getSettingsByCodes(captchaSettingCodes);
    }

    @Override
    public PageResponse<ReportDto> getDashboardList(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int pageSize = pageable.getPageSize();
        String listSql = this.repo.getSqlQuery("CM157.001");

        Query query = entityManager.createNativeQuery(listSql);
        query.setParameter(1, "CM036.001"); // reportType
        query.setParameter(2, offset);      // offset
        query.setParameter(3, pageSize);    // pageSize

        List<Object[]> resultList = query.getResultList();

        // 4️⃣ Map sang DTO
        List<ReportDto> reports = resultList.stream().map(row -> {
            ReportDto dto = new ReportDto();
            dto.setId(row[0] != null ? ((Number) row[0]).longValue() : null);
            dto.setReportCode((String) row[1]);
            dto.setReportGroupCode((String) row[2]);
            dto.setReportCodeName((String) row[3]);
            dto.setReportGroupName((String) row[4]);
            dto.setDataSourceType((String) row[5]);
            dto.setTemplateCode((String) row[6]);
            dto.setReportSource((String) row[7]);
            dto.setSortNumber(row[8] != null ? new BigDecimal(row[8].toString()) : null);
            dto.setGroupSortNumber(row[9] != null ? new BigDecimal(row[9].toString()) : null);
            dto.setActive(false);
            return dto;
        }).toList();

        String countSql = this.repo.getSqlQuery("CM157.002");
        Query countQuery = entityManager.createNativeQuery(countSql);
        countQuery.setParameter("reportType", "CM036.001");
        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        String settingValue = comCfgSettingRepository.findBySettingCode("DASH_DEFAULT")
                .map(ComCfgSetting::getSettingValue)
                .orElse("");

        reports.forEach(r -> r.setActive(settingValue.equalsIgnoreCase(r.getReportCode())));

        return new PageResponse<>(reports, pageable.getPageNumber(), pageable.getPageSize(), totalElements);
    }

    @Override
    public void deleteSetting(String settingCode) {
        ComCfgSetting existing = comCfgSettingRepository.findBySettingCode(settingCode).orElseThrow(() ->
                new BusinessException(CommonErrorCode.NOT_EXIST_SETTING_CODE, settingCode));
        String oldValue = existing.getSettingValue();
        // Nếu là ảnh thì xóa file trên MinIO
        if (SettingCode.LAYOUT.IMAGE_LOGIN.equals(settingCode)
                || SettingCode.LAYOUT.IMAGE_LEFT.equals(settingCode)) {

            String objectName = extractObjectNameFromSettingValue(oldValue);
            if (objectName != null) {
                try {
                    minIOService.deleteFile(objectName);
                } catch (Exception e) {
                    // Silent fail - vẫn xóa record db
                }
            }
        }
        // Xóa record trong db
        comCfgSettingRepository.delete(existing);

    }

    @Override
    public Map<String, Object> getConfigLog() {
        // 1. Các setting code cần lấy trong DB
        List<String> codes = List.of(
                "ACTIVITY_LOG_ENABLED",
                "AUDIT_LOG_ENABLED");

        // 2. Lấy tất cả setting theo code
        List<ComCfgSetting> settings = comCfgSettingRepository.findBySettingCodeIn(codes);

        // 3. Convert sang boolean với default = true (tuỳ bạn chỉnh)
        boolean activityEnabled = getFlag(settings, "ACTIVITY_LOG_ENABLED");
        boolean auditEnabled = getFlag(settings, "AUDIT_LOG_ENABLED");

        // 4. Trả về map với key khớp với lib: activityLogEnabled / auditLogEnabled
        Map<String, Object> result = new HashMap<>();
        result.put("activityLogEnabled", activityEnabled);
        result.put("auditLogEnabled", auditEnabled);

        return result;
    }

    private boolean getFlag(List<ComCfgSetting> settings,
                            String code) {
        return settings.stream()
                .filter(s -> code.equalsIgnoreCase(s.getSettingCode()))
                .findFirst()
                .map(s -> {
                    String v = s.getSettingValue();
                    if (v == null)
                        return true;
                    v = v.trim();
                    return "1".equals(v)
                            || "true".equalsIgnoreCase(v)
                            || "y".equalsIgnoreCase(v)
                            || "yes".equalsIgnoreCase(v);
                })
                .orElse(true);
    }

    @Override
    public AppConfigResponseDto getAppConfig() {
        AppConfigResponseDto config = new AppConfigResponseDto();

        // Lấy layout settings
        List<ComCfgSettingReqDto> layoutSettings = getLayoutPublicSettings();
        config.setLayoutSettings(layoutSettings);

        // Lấy captcha enabled status (tương tự logic trong CaptchaServiceImpl.isCaptchaEnabled())
        boolean captchaEnabled = comCfgSettingRepository
                .findBySettingCode(SettingCode.CAPTCHA.IS_APPLY)
                .map(setting -> Boolean.parseBoolean(setting.getSettingValue()))
                .orElse(false);
        config.setCaptchaEnabled(captchaEnabled);

        // Lấy multitenancy enabled status
        boolean multitenancyEnabled = tenantDbConfigService.isMultitenancyEnabled();
        config.setMultitenancyEnabled(multitenancyEnabled);

        return config;
    }
}



