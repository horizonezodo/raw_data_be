package ngvgroup.com.loan.feature.product_proccess.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;
import ngvgroup.com.loan.feature.product_proccess.service.ProductValidationService;
import org.springframework.stereotype.Service;

@Service
public class ProductValidationServiceImpl implements ProductValidationService {
    @Override
    public void validateProductInfo(ProductProfileDTO profile) {
        if (profile == null || profile.getLnmProductCode() == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        validateRequest(profile);
    }

        private void validateRequest(ProductProfileDTO dto) {
        validateString(dto.getOrgCode(), "orgCode");
        validateString(dto.getLnmProductCode(), "lnmProductCode");
        validateString(dto.getLnmProductName(), "lnmProductName");
        validateString(dto.getCurrencyCode(), "currencyCode");
        validateString(dto.getLoanTermTypeCode(), "loanTermTypeCode");
        validateString(dto.getLnmProductTypeCode(), "lnmProductTypeCode");
        validateString(dto.getInterestRateCode(), "interestRateCode");
        validateString(dto.getAccClassCode(), "accClassCode");
        validateString(dto.getAccStructureCode(), "accStructureCode");
    }

    private void validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(LoanErrorCode.VALIDATION_ERROR, fieldName);
        }
    }
}
