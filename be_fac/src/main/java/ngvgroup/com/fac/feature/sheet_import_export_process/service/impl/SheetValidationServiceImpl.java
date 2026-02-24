package ngvgroup.com.fac.feature.sheet_import_export_process.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.AccountEntryDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.SheetValidationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
public class SheetValidationServiceImpl implements SheetValidationService {
    @Override
    public void validateSheetInfo(SheetInfoDto dto) {
        if (dto == null) {
            throw new BusinessException(FacErrorCode.ERROR_DATA_EMPTY);
        }

        if (!StringUtils.hasText(dto.getVoucherTypeCode())) {
            throw new BusinessException(FacErrorCode.ERROR_VOUCHER_TYPE_REQ);
        }
        if (dto.getTotalForeignAmt() == null) {
            throw new BusinessException(FacErrorCode.ERROR_TOTAL_AMT_REQ);
        }
        if (!StringUtils.hasText(dto.getTxnContent())) {
            throw new BusinessException(FacErrorCode.ERROR_CONTENT_REQ);
        }
        validateObjectInfo(dto);
        validateAccountingEntries(dto);
    }

    private void validateObjectInfo(SheetInfoDto dto) {
        if (!StringUtils.hasText(dto.getObjectTypeCode())) {
            throw new BusinessException(FacErrorCode.ERROR_OBJ_TYPE_REQ);
        }

        if (!StringUtils.hasText(dto.getObjectTxnName())) {
            throw new BusinessException(FacErrorCode.ERROR_OBJ_NAME_REQ);
        }

        if (!StringUtils.hasText(dto.getIdentificationId())) {
            throw new BusinessException(FacErrorCode.ERROR_ID_REQ);
        }

        if (dto.getIssueDate() == null) {
            throw new BusinessException(FacErrorCode.ERROR_ISSUE_DATE_REQ);
        }

        if (!StringUtils.hasText(dto.getIssuePlace())) {
            throw new BusinessException(FacErrorCode.ERROR_ISSUE_PLACE_REQ);
        }

        if (!StringUtils.hasText(dto.getAddress())) {
            throw new BusinessException(FacErrorCode.ERROR_ADDRESS_REQ);
        }
    }

    private void validateAccountingEntries(SheetInfoDto dto) {
        if (CollectionUtils.isEmpty(dto.getAccounts())) {
            throw new BusinessException(FacErrorCode.ERROR_EMPTY_ENTRIES);
        }

        for (AccountEntryDto entry : dto.getAccounts()) {

            if (!StringUtils.hasText(entry.getAccClassCode())) {
                throw new BusinessException(FacErrorCode.ERROR_ACC_CLASS_REQ);
            }

            if (!StringUtils.hasText(entry.getAccNo())) {
                throw new BusinessException(FacErrorCode.ERROR_ACC_NO_REQ);
            }

            BigDecimal lineAmt = entry.getLineForeignAmt() != null ? entry.getLineForeignAmt() : BigDecimal.ZERO;
            if (lineAmt.compareTo(dto.getTotalForeignAmt()) != 0) {
                throw new BusinessException(FacErrorCode.ERROR_AMT_MISMATCH);
            }
        }
    }

}
