package ngvgroup.com.fac.feature.single_entry_acct.service;

import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;

public interface SingleEntryAcctValidatorService {
    //Validate request
    void validateRequest(SingleEntryAcctDTO dto);
}
