package ngvgroup.com.loan.feature.interest_process.service;

import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;

public interface InterestValidationService {
    void validateCustomerInfo(InterestProfileDTO profile);
}
