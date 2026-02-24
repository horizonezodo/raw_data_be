package ngvgroup.com.fac.feature.single_entry_acct.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctValidatorService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SingleEntryAcctValidatorServiceImpl implements SingleEntryAcctValidatorService {

    @Override
    public void validateRequest(SingleEntryAcctDTO dto) {
        if (dto == null) {
            throw new BusinessException(FacErrorCode.DATA_NOT_FOUND);
        }
    }
}
