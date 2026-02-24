package ngvgroup.com.crm.features.customer.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.crm.core.constant.CrmErrorCode;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.crm.features.customer.dto.profile.*;
import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCust;
import ngvgroup.com.crm.features.customer.repository.inf.CrmInfCustRepository;
import ngvgroup.com.crm.features.customer.repository.txn.CrmTxnCustRepository;
import ngvgroup.com.crm.features.customer.service.CustomerValidationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerValidationServiceImpl implements CustomerValidationService {

    private final CrmTxnCustRepository txnCustRepo;
    private final CrmInfCustRepository infCustRepo;

    // Regex: Số điện thoại bắt đầu bằng 0, theo sau là 9-10 chữ số
    private static final String REGEX_MOBILE = "0\\d{9,10}";
    // Regex: Email tiêu chuẩn
    private static final String REGEX_EMAIL = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public void validateCustomerInfo(CustomerProfileDTO profile) {
        if (profile == null || profile.getBasicInfo() == null) {
            throw new BusinessException(CrmErrorCode.MISSING_CUSTOMER_DATA, "Dữ liệu khách hàng không được để trống");
        }

        // 1. Validate Thông tin chung (General Info)
        validateGeneralInfo(profile.getBasicInfo());

        // 2. Validate Thông tin định danh (Identity - Cá nhân/Doanh nghiệp)
        validateIdentityInfo(profile);

        // 3. Validate Địa chỉ (Address)
        validateAddressInfo(profile.getAddressInfo());

        // 4. Validate Thông tin mở rộng (Extension)
        validateExtensionInfo(profile.getExtendedInfo());
    }

    private void validateGeneralInfo(CustomerGeneralInfoDTO gen) {
        requireNotEmpty(gen.getCustomerName(), "Tên khách hàng");
        requireNotEmpty(gen.getCustomerType(), "Loại khách hàng");
        requireNotEmpty(gen.getOrgCode(), "Chi nhánh quản lý");
        requireNotEmpty(gen.getAreaCode(), "Khu vực");

        // Validate Mobile
        requireNotEmpty(gen.getMobileNumber(), "Số điện thoại");
        if (!gen.getMobileNumber().matches(REGEX_MOBILE)) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Số điện thoại không đúng định dạng");
        }
        // Check trùng SĐT (IsIdCard = false)
        checkUniqueInDb(gen.getMobileNumber(), gen.getCustomerCode(), CrmErrorCode.MOBILE_NUMBER_EXIST, false);

        // Validate Email
        if (StringUtils.hasText(gen.getEmail()) && !gen.getEmail().matches(REGEX_EMAIL)) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Email không đúng định dạng");
        }

        if (gen.getIsInsurance() == null) {
            throw new BusinessException(CrmErrorCode.MISSING_INSURANCE_INFO, "Thông tin bảo hiểm tiền gửi là bắt buộc");
        }
    }

    private void validateIdentityInfo(CustomerProfileDTO p) {
        String custType = p.getBasicInfo().getCustomerType();
        String custCode = p.getBasicInfo().getCustomerCode();

        if (CrmVariableConstants.CUSTOMER_TYPE_INDIVIDUAL.equalsIgnoreCase(custType)) {
            validateIndividualIdentity(p.getIdentityInfoPersonal(), custCode);
        } else {
            validateCorporateIdentity(p.getIdentityInfoCompany(), custCode);
        }
    }

    private void validateIndividualIdentity(CustomerIndividualInfoDTO indv, String custCode) {
        if (indv == null) {
            throw new BusinessException(CrmErrorCode.MISSING_PERSONAL_INFO, "Thiếu thông tin cá nhân");
        }

        // Basic Validations
        validateDateOfBirth(indv.getDateOfBirth());
        requireNotEmpty(indv.getIdentificationType(), "Loại giấy tờ tùy thân");
        requireNotEmpty(indv.getIdentificationId(), "Số giấy tờ tùy thân");

        // Check Unique
        checkUniqueInDb(indv.getIdentificationId(), custCode, CrmErrorCode.CCCD_EXIST, true);

        // Date Logic
        validateIndividualIdDates(indv);

        requireNotEmpty(indv.getIssuePlace(), "Nơi cấp");
    }

    private void validateIndividualIdDates(CustomerIndividualInfoDTO indv) {
        if (indv.getIssueDate() == null) {
            throw new BusinessException(CrmErrorCode.NOT_NULL, "Ngày cấp");
        }
        if (indv.getIssueDate().after(new Date())) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Ngày cấp không được ở tương lai");
        }
        if (indv.getExpiryDate() == null) {
            throw new BusinessException(CrmErrorCode.NOT_NULL, "Ngày hết hạn");
        }
        if (indv.getExpiryDate().before(indv.getIssueDate())) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Ngày hết hạn phải sau ngày cấp");
        }
    }

    private void validateCorporateIdentity(CustomerCorporateInfoDTO corp, String custCode) {
        if (corp == null) {
            throw new BusinessException(CrmErrorCode.MISSING_CORPORATE_INFO, "Thiếu thông tin doanh nghiệp");
        }

        requireNotEmpty(corp.getBusinessLicenseNo(), "Số ĐKKD");

        // Check Unique (License treated as ID)
        checkUniqueInDb(corp.getBusinessLicenseNo(), custCode, CrmErrorCode.CCCD_EXIST, true);

        if (corp.getBusinessLicenseDate() == null) {
            throw new BusinessException(CrmErrorCode.NOT_NULL, "Ngày cấp ĐKKD");
        }
        requireNotEmpty(corp.getIssuedBy(), "Cơ quan cấp ĐKKD");

        // Legal Representative
        requireNotEmpty(corp.getLegalRepName(), "Tên người đại diện");
        requireNotEmpty(corp.getLegalRepTitle(), "Chức vụ người đại diện");
        requireNotEmpty(corp.getLegalRepIdNo(), "Số giấy tờ người đại diện");
    }

    private void validateAddressInfo(List<CustomerAddressDTO> addresses) {
        if (CollectionUtils.isEmpty(addresses)) {
            throw new BusinessException(CrmErrorCode.MISSING_ADDRESS, "Phải nhập ít nhất một địa chỉ");
        }

        int primaryCount = 0;
        for (CustomerAddressDTO addr : addresses) {
            requireNotEmpty(addr.getProvinceCode(), "Tỉnh/Thành phố");
            requireNotEmpty(addr.getWardCode(), "Phường/Xã"); // District thường đi kèm Ward
            requireNotEmpty(addr.getAddress(), "Địa chỉ chi tiết");

            if (Integer.valueOf(1).equals(addr.getIsPrimary())) {
                primaryCount++;
            }
        }

        if (primaryCount != 1) {
            throw new BusinessException(CrmErrorCode.INVALID_PRIMARY_ADDRESS,
                    "Phải có duy nhất một địa chỉ chính (Thường trú/Trụ sở)");
        }
    }

    private void validateExtensionInfo(CustomerExtensionInfoDTO ext) {
        if (ext == null)
            return; // Thông tin mở rộng có thể null tùy nghiệp vụ

        // Nếu là hộ nghèo -> Bắt buộc nhập số sổ
        if (Integer.valueOf(1).equals(ext.getIsPoorHousehold())) {
            requireNotEmpty(ext.getPoorHouseholdBookNo(), "Số sổ hộ nghèo");
        }

        // Nếu có nhập thâm niên -> Phải có đơn vị tính
        if (ext.getWorkTimeValue() != null && !StringUtils.hasText(ext.getWorkTimeUnit())) {
            throw new BusinessException(CrmErrorCode.MISSING_WORK_TIME_UNIT,
                    "Vui lòng chọn đơn vị tính cho thâm niên công tác");
        }
    }

    /**
     * Hàm check trùng lặp trong DB (Cả bảng TXN và INF)
     * Logic:
     * 1. Check bảng TXN (đang chờ duyệt): Trừ bản ghi có trạng thái CANCEL.
     * 2. Check bảng INF (đã duyệt):
     * -> Nếu customerCode != null (Update): Loại trừ chính bản ghi đang sửa.
     */
    private void checkUniqueInDb(String value, String currentCustCode, ErrorCode errorCode, boolean isIdCard) {
        if (!StringUtils.hasText(value)) {
            return;
        }

        String safeCustCode = currentCustCode != null ? currentCustCode : "";

        // 1. Check active transactions
        if (existsInTransaction(value, safeCustCode, isIdCard)) {
            throw new BusinessException(errorCode);
        }

        // 2. Check completed records
        if (existsInInfo(value, currentCustCode, isIdCard)) {
            throw new BusinessException(errorCode);
        }
    }

    private boolean existsInTransaction(String value, String custCode, boolean isIdCard) {
        if (isIdCard) {
            return txnCustRepo.existsByIdentificationIdAndBusinessStatus(
                    value, VariableConstants.CANCEL, custCode);
        }
        return txnCustRepo.existsByMobileNumberAndBusinessStatus(
                value, VariableConstants.CANCEL, custCode);
    }

    private boolean existsInInfo(String value, String custCode, boolean isIdCard) {
        boolean hasCustCode = StringUtils.hasText(custCode);

        if (isIdCard) {
            return hasCustCode ? infCustRepo.existsByIdentificationId(value, custCode)
                    : infCustRepo.existsByIdentificationId(value);
        }

        return hasCustCode ? infCustRepo.existsByMobileNumber(value, custCode)
                : infCustRepo.existsByMobileNumber(value);
    }

    private void validateDateOfBirth(Date dob) {
        if (dob == null) {
            throw new BusinessException(CrmErrorCode.NOT_NULL, "Ngày sinh");
        }
        Date now = new Date();
        if (dob.after(now)) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Ngày sinh không được lớn hơn ngày hiện tại");
        }

        // Logic kiểm tra tuổi tối đa (ví dụ 120 tuổi)
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.YEAR, -120);
        if (dob.before(c.getTime())) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Năm sinh không hợp lệ (Quá 120 tuổi)");
        }
    }

    private void requireNotEmpty(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(CrmErrorCode.NOT_NULL, fieldName);
        }
    }

    @Override
    public void validateCustomerCode(CustomerProfileDTO profile) {
        Optional<CrmTxnCust> cust = txnCustRepo.findByCustomerCode(profile.getBasicInfo().getCustomerCode(),
                List.of(VariableConstants.CANCEL, VariableConstants.COMPLETE));
        if (cust.isPresent()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Khách hàng đang có giao dịch chờ duyệt");
        }
    }
}