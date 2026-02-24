package ngvgroup.com.fac.feature.double_entry_accounting.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryValidationService;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct.FacTxnAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry.FacTxnAcctEntryDTO;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry_dtl.FacTxnAcctEntryDtlDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class DoubleEntryValidationServiceImpl implements DoubleEntryValidationService {

    @Override
    public void validateDoubleEntry(DoubleEntryAccountingProcessDto dto){
        validateAcctInfo(dto.getFacTxnAcctEntryDtlDtos());
        validateTransactionInfo(dto.getFacTxnAcctEntryDto());
        validateSubjectInfo(dto.getFacTxnAcctDTO());
    }


    private void validateTransactionInfo(FacTxnAcctEntryDTO facTxnAcctEntryDTO){

        requireNotEmpty(facTxnAcctEntryDTO.getVoucherTypeCode(),"Loại chứng từ");

        requireNotEmpty(facTxnAcctEntryDTO.getEntryContent(),"Nội dung phát sinh");

    }

    private void validateSubjectInfo(FacTxnAcctDTO facTxnAcctDTO){
        requireNotEmpty(facTxnAcctDTO.getOrgCode(),"Mã chi nhánh");
        requireNotEmpty(facTxnAcctDTO.getTxnDate(),"Ngày giao dịch");
        requireNotEmpty(facTxnAcctDTO.getTxnTime(),"Thời gian giao dịch");
        requireNotEmpty(facTxnAcctDTO.getTxnContent(),"Diễn giải");
        requireNotEmpty(facTxnAcctDTO.getCurrencyCode(),"Mã tiền tệ");
        requireNotEmpty(facTxnAcctDTO.getTotalForeignAmt(),"Tổng tiền nguyên tệ");
        requireNotEmpty(facTxnAcctDTO.getTotalBaseAmt(),"Tổng tiền quy đổi");
    }

    private void validateAcctInfo(List<FacTxnAcctEntryDtlDTO> details) {

        for (int i = 0; i < details.size(); i++) {
            FacTxnAcctEntryDtlDTO dto = details.get(i);

            requireNotEmpty(dto.getEntryType(), "Loại bút toán ");
            requireNotEmpty(dto.getAccNo(), "Số tài khoản ");
            requireNotEmpty(dto.getAccClassCode(), "Mã phân loại tài khoản nội bộ [");
            requireNotEmpty(dto.getAccCoaCode(), "Mã tài khoản tiêu chuẩn ");
            requireNotEmpty(dto.getLineBaseAmt(), "Số tiền quy đổi phát sinh chi tiết ");
            requireNotEmpty(dto.getLineForeignAmt(), "Số tiền nguyên tệ phát sinh chi tiết ");
        }
    }

    private void requireNotEmpty(Object value, String fieldName) {

        if (value == null) {
            throw new BusinessException(FacErrorCode.NOT_NULL, fieldName);
        }

        if (value instanceof String str && !StringUtils.hasText(str)) {
            throw new BusinessException(FacErrorCode.NOT_NULL, fieldName);
        }
    }

}
