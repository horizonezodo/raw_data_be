package ngvgroup.com.bpm.features.transaction.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.core.contants.BpmErrorCode;
import ngvgroup.com.bpm.core.contants.StatusConstants;
import ngvgroup.com.bpm.features.transaction.dto.TransactionOutboxExcel;
import ngvgroup.com.bpm.features.transaction.dto.TransactionDto;
import ngvgroup.com.bpm.core.base.repository.BpmTxnProcessInstanceRepository;
import ngvgroup.com.bpm.features.transaction.filter.TransactionFilter;
import ngvgroup.com.bpm.features.transaction.service.TransactionOutboxService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionOuboxServiceImpl extends BaseStoredProcedureService implements TransactionOutboxService {

    private final BpmTxnProcessInstanceRepository bpmTxnProcessInstanceRepository;
    private final ExportExcel exportExcel;

    @Override
    public Page<TransactionDto> getOutbox(String keyword, TransactionFilter transactionFilter, Pageable pageable) {
        TransactionInboxServiceImpl.prepareTransactionFilter(transactionFilter);

        transactionFilter.setUserId(getCurrentUserId());
        transactionFilter.setUserName(getCurrentUserName());
        transactionFilter.setBusinessStatus(StatusConstants.COMPLETE);
        Page<TransactionDto> page = bpmTxnProcessInstanceRepository.getTaskOutbox(keyword, transactionFilter, pageable);
        page.forEach(item -> {
            item.setUserName(getCurrentUserName());
            item.setUserId(getCurrentUserId());
        });

        return page;
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(TransactionFilter transactionFilter, String fileName) {

        TransactionInboxServiceImpl.prepareTransactionFilter(transactionFilter);

        transactionFilter.setUserId(getCurrentUserId());
        transactionFilter.setUserName(getCurrentUserName());
        transactionFilter.setBusinessStatus(StatusConstants.COMPLETE);
        List<TransactionOutboxExcel> taskOutboxExcels = bpmTxnProcessInstanceRepository
                .exportToExcelTaskOutbox(transactionFilter);

        taskOutboxExcels.forEach(dto -> {
            Timestamp updateTime = dto.getTaskUpdateTime();
            Timestamp deadline = dto.getSlaTaskDeadline();

            if (updateTime != null && deadline != null && updateTime.after(deadline)) {
                dto.setSlaStatus(StatusConstants.BREACHED);
            } else {
                dto.setSlaStatus(StatusConstants.ACHIEVED);
            }
        });

        try {
            return exportExcel.exportExcel(taskOutboxExcels, fileName);
        } catch (Exception e) {
            throw new BusinessException(BpmErrorCode.WRITE_EXCEL_ERROR);
        }
    }
}
