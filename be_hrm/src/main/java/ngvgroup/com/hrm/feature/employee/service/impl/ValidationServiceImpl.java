package ngvgroup.com.hrm.feature.employee.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.hrm.core.constant.HrmErrorCode;
import ngvgroup.com.hrm.feature.employee.dto.HrmBasicInfoDto;
import ngvgroup.com.hrm.feature.employee.dto.HrmEduInfoDto;
import ngvgroup.com.hrm.feature.employee.dto.HrmPositionInfoDto;
import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;
import ngvgroup.com.hrm.feature.employee.repository.HrmInfEmployeeRepository;
import ngvgroup.com.hrm.feature.employee.repository.HrmTxnEmployeeRepository;
import ngvgroup.com.hrm.feature.employee.service.ValidationSerivce;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationSerivce {

    private static final String CANCEL_STATUS = "CANCEL";

    private final HrmInfEmployeeRepository infEmployeeRepository;
    private final HrmTxnEmployeeRepository txnEmployeeRepository;

    @Override
    public void validateHrmProfile(HrmProfileDto profileDto) {
        if (profileDto == null) {
            throw new BusinessException(HrmErrorCode.BUSINESS_DATA_MISSING);
        }

        // [1]. Validate Thông tin chung (Basic Info)
        validateBasicInfo(profileDto.getBasicInfo());

        // [2]. Validate Thông tin chức vụ (Position Info)
        validatePositionInfos(profileDto.getPositionInfos());

        // [3]. Validate Trình độ học vấn (Education Info)
        validateEducationInfos(profileDto.getEduInfos());
    }

    /**
     * Validate basic employee information
     */
    private void validateBasicInfo(HrmBasicInfoDto basicInfo) {
        if (basicInfo == null) {
            throw new BusinessException(HrmErrorCode.MISSING_REQUIRED_FIELD, "Thông tin chung");
        }

        String employeeCode = basicInfo.getEmployeeCode();

        // Required field validations
        validateRequiredField(basicInfo.getEmployeeTypeCode(), "Loại nhân viên");
        validateRequiredField(basicInfo.getOrgCode(), "Chi nhánh");
        validateRequiredField(basicInfo.getEmployeeName(), "Tên nhân viên");
        validateRequiredDate(basicInfo.getDateOfBirth(), "Ngày sinh");
        validateRequiredField(basicInfo.getGenderCode(), "Giới tính");
        validateRequiredField(basicInfo.getMobileNumber(), "Số di động");
        validateRequiredField(basicInfo.getEmail(), "Email");
        validateRequiredField(basicInfo.getCurrentAddress(), "Địa chỉ hiện tại");
        validateRequiredField(basicInfo.getPermanentAddress(), "Địa chỉ thường trú");
        validateRequiredField(basicInfo.getIdentificationId(), "Số định danh");
        validateRequiredDate(basicInfo.getIssueDate(), "Ngày cấp");
        validateRequiredDate(basicInfo.getExpiryDate(), "Ngày hết hiệu lực");
        validateRequiredField(basicInfo.getIssuePlace(), "Nơi cấp");
        validateRequiredDate(basicInfo.getJoinDate(), "Ngày vào làm việc");
        validateRequiredDate(basicInfo.getConfirmDate(), "Ngày chính thức");

        // Date range validations
        // Ngày hết hiệu lực >= Ngày cấp
        // Date range validations
        // Ngày hết hiệu lực >= Ngày cấp
        if (basicInfo.getExpiryDate() != null && basicInfo.getIssueDate() != null
                && basicInfo.getExpiryDate().isBefore(basicInfo.getIssueDate())) {
            throw new BusinessException(HrmErrorCode.INVALID_EXPIRY_DATE);
        }

        // Ngày chính thức >= Ngày vào làm việc
        if (basicInfo.getConfirmDate() != null && basicInfo.getJoinDate() != null
                && basicInfo.getConfirmDate().isBefore(basicInfo.getJoinDate())) {
            throw new BusinessException(HrmErrorCode.INVALID_CONFIRM_DATE);
        }

        // Uniqueness validations for mobile number
        validateMobileNumberUniqueness(basicInfo.getMobileNumber(), employeeCode);

        // Uniqueness validations for identification ID
        validateIdentificationIdUniqueness(basicInfo.getIdentificationId(), employeeCode);
    }

    /**
     * Validate mobile number uniqueness across TXN and INF tables
     */
    private void validateMobileNumberUniqueness(String mobileNumber, String employeeCode) {
        if (!StringUtils.hasText(mobileNumber)) {
            return;
        }

        boolean existsInTxn;
        boolean existsInInf;
        existsInTxn = txnEmployeeRepository.existsByMobileNumberAndEmployeeCodeNotAndBusinessStatusNot(
                mobileNumber, employeeCode, CANCEL_STATUS);
        existsInInf = infEmployeeRepository.existsByMobileNumberAndEmployeeCodeNot(mobileNumber, employeeCode);

        if (existsInTxn || existsInInf) {
            throw new BusinessException(HrmErrorCode.PHONE_EXIST, mobileNumber);
        }
    }

    /**
     * Validate identification ID uniqueness across TXN and INF tables
     */
    private void validateIdentificationIdUniqueness(String identificationId, String employeeCode) {
        if (!StringUtils.hasText(identificationId)) {
            return;
        }

        boolean existsInTxn;
        boolean existsInInf;

        existsInTxn = txnEmployeeRepository.existsByIdentificationIdAndEmployeeCodeNotAndBusinessStatusNot(
                identificationId, employeeCode, CANCEL_STATUS);
        existsInInf = infEmployeeRepository.existsByIdentificationIdAndEmployeeCodeNot(identificationId,
                employeeCode);

        if (existsInTxn || existsInInf) {
            throw new BusinessException(HrmErrorCode.IDENTIFICATION_ID_EXIST, identificationId);
        }
    }

    /**
     * Validate position information list
     */
    private void validatePositionInfos(List<HrmPositionInfoDto> positionInfos) {
        // Must have at least 1 position record
        if (CollectionUtils.isEmpty(positionInfos)) {
            throw new BusinessException(HrmErrorCode.MISSING_POSITION_INFO);
        }

        LocalDate today = LocalDate.now();
        Set<String> positionKeys = new HashSet<>();
        int activePositionCount = 0;
        int nullValidToCount = 0;

        for (int i = 0; i < positionInfos.size(); i++) {
            HrmPositionInfoDto position = positionInfos.get(i);

            validateSinglePosition(position, i, positionKeys);

            // Count null validTo
            if (position.getValidTo() == null) {
                nullValidToCount++;
            }

            // Check active position
            if (isPositionActive(position, today)) {
                activePositionCount++;
            }
        }

        // Must not have multiple records with validTo = null
        if (nullValidToCount > 1) {
            throw new BusinessException(HrmErrorCode.OVERLAPPING_POSITION_VALIDITY);
        }

        // Check for overlapping validity periods
        validateNoOverlappingPositions(positionInfos);

        // Must have exactly 1 active position
        if (activePositionCount == 0) {
            throw new BusinessException(HrmErrorCode.MISSING_ACTIVE_POSITION);
        }
    }

    /**
     * Validate individual position fields and logic
     */
    private void validateSinglePosition(HrmPositionInfoDto position, int index, Set<String> positionKeys) {
        // Required field validations
        validateRequiredField(position.getOrgCode(), "Đơn vị làm việc (dòng " + (index + 1) + ")");
        validateRequiredField(position.getOrgUnitCode(), "Phòng ban (dòng " + (index + 1) + ")");
        validateRequiredField(position.getPositionCode(), "Chức vụ (dòng " + (index + 1) + ")");
        validateRequiredDate(position.getValidFrom(), "Ngày hiệu lực (dòng " + (index + 1) + ")");

        // Date range validation: validFrom < validTo
        if (position.getValidFrom() != null && position.getValidTo() != null
                && !position.getValidFrom().isBefore(position.getValidTo())) {
            throw new BusinessException(HrmErrorCode.INVALID_POSITION_VALIDITY);
        }

        // Check for duplicate position (same all fields)
        String positionKey = buildPositionKey(position);
        if (!positionKeys.add(positionKey)) {
            throw new BusinessException(HrmErrorCode.DUPLICATE_POSITION);
        }
    }

    /**
     * Build unique key for position record
     */
    private String buildPositionKey(HrmPositionInfoDto position) {
        return String.format("%s|%s|%s|%s|%s",
                position.getOrgCode(),
                position.getOrgUnitCode(),
                position.getPositionCode(),
                position.getValidFrom(),
                position.getValidTo());
    }

    /**
     * Check if position is currently active
     */
    private boolean isPositionActive(HrmPositionInfoDto position, LocalDate today) {
        if (position.getValidFrom() == null) {
            return false;
        }

        // Active if: validFrom <= today AND (validTo is null OR validTo >= today)
        boolean validFromOk = !position.getValidFrom().isAfter(today);
        boolean validToOk = position.getValidTo() == null || !position.getValidTo().isBefore(today);

        return validFromOk && validToOk;
    }

    /**
     * Validate no overlapping validity periods between positions
     */
    private void validateNoOverlappingPositions(List<HrmPositionInfoDto> positionInfos) {
        for (int i = 0; i < positionInfos.size(); i++) {
            for (int j = i + 1; j < positionInfos.size(); j++) {
                if (isOverlapping(positionInfos.get(i), positionInfos.get(j))) {
                    throw new BusinessException(HrmErrorCode.OVERLAPPING_POSITION_VALIDITY);
                }
            }
        }
    }

    /**
     * Check if two position periods overlap
     */
    private boolean isOverlapping(HrmPositionInfoDto p1, HrmPositionInfoDto p2) {
        LocalDate start1 = p1.getValidFrom();
        LocalDate end1 = p1.getValidTo();
        LocalDate start2 = p2.getValidFrom();
        LocalDate end2 = p2.getValidTo();

        if (start1 == null || start2 == null) {
            return false;
        }

        // If both have no end date, they overlap (infinite)
        if (end1 == null && end2 == null) {
            return true;
        }

        // If one has no end date
        if (end1 == null) {
            // p1 is infinite, check if p2 starts before or during p1
            return !start2.isBefore(start1) || !end2.isBefore(start1);
        }
        if (end2 == null) {
            // p2 is infinite, check if p1 starts before or during p2
            return !start1.isBefore(start2) || !end1.isBefore(start2);
        }

        // Both have end dates - standard overlap check
        // Overlap if: start1 <= end2 AND start2 <= end1
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

    /**
     * Validate education information list
     */
    private void validateEducationInfos(List<HrmEduInfoDto> eduInfos) {
        // Must have at least 1 education record
        if (CollectionUtils.isEmpty(eduInfos)) {
            throw new BusinessException(HrmErrorCode.MISSING_EDUCATION_INFO);
        }

        for (int i = 0; i < eduInfos.size(); i++) {
            HrmEduInfoDto edu = eduInfos.get(i);
            String rowLabel = " (dòng " + (i + 1) + ")";

            // Required field validations
            validateRequiredField(edu.getEduLevelCode(), "Trình độ học vấn" + rowLabel);
            validateRequiredField(edu.getEduTypeCode(), "Loại hình đào tạo" + rowLabel);
            validateRequiredLong(edu.getFromYear(), "Từ năm" + rowLabel);
            validateRequiredLong(edu.getToYear(), "Đến năm" + rowLabel);
            validateRequiredField(edu.getSchoolName(), "Tên trường" + rowLabel);
            validateRequiredField(edu.getFacultyName(), "Tên khoa" + rowLabel);
            validateRequiredField(edu.getMajorName(), "Tên chuyên ngành" + rowLabel);

            // Year range validation: fromYear <= toYear
            if (edu.getFromYear() != null && edu.getToYear() != null && edu.getFromYear() > edu.getToYear()) {
                throw new BusinessException(HrmErrorCode.INVALID_EDUCATION_YEAR);
            }

            // If graduated, graduation fields are required
            if (edu.getIsGraduated() != null && edu.getIsGraduated() == 1L) {
                validateRequiredLong(edu.getGraduationYear(), "Năm tốt nghiệp" + rowLabel);
                validateRequiredLong(edu.getCertIssueDate(), "Ngày cấp bằng" + rowLabel);
                validateRequiredField(edu.getClassificationCode(), "Xếp loại" + rowLabel);
            }
        }
    }

    /**
     * Validate required string field
     */
    private void validateRequiredField(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(HrmErrorCode.MISSING_REQUIRED_FIELD, fieldName);
        }
    }

    /**
     * Validate required date field
     */
    private void validateRequiredDate(LocalDate value, String fieldName) {
        if (value == null) {
            throw new BusinessException(HrmErrorCode.MISSING_REQUIRED_FIELD, fieldName);
        }
    }

    /**
     * Validate required Long field
     */
    private void validateRequiredLong(Long value, String fieldName) {
        if (value == null) {
            throw new BusinessException(HrmErrorCode.MISSING_REQUIRED_FIELD, fieldName);
        }
    }
}
