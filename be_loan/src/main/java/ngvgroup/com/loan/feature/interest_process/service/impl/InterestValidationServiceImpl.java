package ngvgroup.com.loan.feature.interest_process.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;
import ngvgroup.com.loan.feature.interest_process.service.InterestValidationService;
import org.springframework.stereotype.Service;

@Service
public class InterestValidationServiceImpl implements InterestValidationService {
    @Override
    public void validateCustomerInfo(InterestProfileDTO profile) {
        validateRequest(profile);
    }

    public void validateRequest(InterestProfileDTO dto) {
        validateString(dto.getOrgCode(), "orgCode");
        validateString(dto.getInterestRateCode(), "interestRateCode");
        validateString(dto.getInterestRateName(), "interestRateName");
        validateString(dto.getInterestRateType(), "interestRateType");
        validateString(dto.getCurrencyCode(), "currencyCode");
    }

    public void validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(LoanErrorCode.VALIDATION_ERROR, fieldName);
        }
    }
}
