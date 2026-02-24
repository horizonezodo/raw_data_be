package ngvgroup.com.bpm.features.transaction.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.core.base.repository.BpmTxnProcessInstanceRepository;
import ngvgroup.com.bpm.core.contants.BpmErrorCode;
import ngvgroup.com.bpm.core.contants.StatusConstants;
import ngvgroup.com.bpm.features.transaction.dto.TransactionDto;
import ngvgroup.com.bpm.features.transaction.dto.TransactionInboxExcel;
import ngvgroup.com.bpm.features.transaction.filter.TransactionFilter;
import ngvgroup.com.bpm.features.transaction.service.TransactionInboxService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionInboxServiceImpl extends BaseStoredProcedureService implements TransactionInboxService {

    private final BpmTxnProcessInstanceRepository bpmTxnProcessInstanceRepository;
    private final ExportExcel exportExcel;

    @Override
    public Page<TransactionDto> getTaskInbox(String keyword, TransactionFilter transactionFilter, Pageable pageable) {

        List<String> processTypeCodes = transactionFilter.getProcessTypeCodes();

        if (processTypeCodes != null && processTypeCodes.isEmpty()) {
            transactionFilter.setProcessTypeCodes(processTypeCodes);
        }
        if (transactionFilter.getToDate() != null) {
            transactionFilter.setToDate(
                    Timestamp.valueOf(transactionFilter.getToDate().toLocalDateTime().plusDays(1)));
        }
        transactionFilter.setUserId(getCurrentUserId());

        transactionFilter.setUserName(getCurrentUserName());
        transactionFilter.setBusinessStatus(StatusConstants.COMPLETE);

        Page<TransactionDto> page = bpmTxnProcessInstanceRepository.getTaskInbox(keyword, transactionFilter, pageable);

        // Duyệt danh sách và tính SLA
        page.forEach(item -> {
            item.setUserName(getCurrentUserName());
            item.setUserId(getCurrentUserId());

            // Gọi hàm tính toán nội bộ
            String status = calculateSlaStatus(
                    item.getSlaResult(),
                    item.getSlaTaskDeadline(),
                    item.getSlaMaxDuration(),
                    item.getSlaWarningType(),
                    item.getSlaWarningDuration(),
                    item.getSlaWarningPercent() // Bắt buộc DTO phải có field này
            );
            item.setSlaStatus(status);
            if (item.getSlaTaskDeadline() != null && item.getCreateDate() != null) {
                long now = System.currentTimeMillis();
                long start = item.getCreateDate().getTime();
                long end = item.getSlaTaskDeadline().getTime();

                if (end > start) {
                    // Tính phần trăm đã trôi qua
                    double percent = (double) (now - start) / (end - start) * 100.0;

                    // Đảm bảo nằm trong khoảng 0% - 100%
                    long finalPercent = (long) Math.min(Math.max(percent, 0.0), 100.0);

                    item.setProgressPercent(finalPercent);
                } else {
                    // Trường hợp deadline bị set trước cả ngày tạo (lỗi dữ liệu), set 100%
                    item.setProgressPercent(100L);
                }
            }
        });

        return page;
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(TransactionFilter transactionFilter, String fileName) {

        prepareTransactionFilter(transactionFilter);
        transactionFilter.setBusinessStatus(StatusConstants.COMPLETE);

        List<TransactionInboxExcel> transactionDtos = bpmTxnProcessInstanceRepository
                .exportToExcelInbox(transactionFilter);

        // Duyệt danh sách và tính SLA
        transactionDtos.forEach(dto -> {
            // Gọi hàm tính toán nội bộ
            String status = calculateSlaStatus(
                    dto.getSlaResult(),
                    dto.getSlaTaskDeadline(),
                    dto.getSlaMaxDuration(),
                    dto.getSlaWarningType(),
                    dto.getSlaWarningDuration(),
                    dto.getSlaWarningPercent() // Bắt buộc DTO Excel phải có field này
            );
            dto.setSlaStatus(status);
        });

        try {
            return exportExcel.exportExcel(transactionDtos, fileName);
        } catch (Exception e) {
            throw new BusinessException(BpmErrorCode.WRITE_EXCEL_ERROR);
        }
    }

    static void prepareTransactionFilter(TransactionFilter transactionFilter) {
        List<String> processTypeCodes = transactionFilter.getProcessTypeCodes();

        if (processTypeCodes != null && processTypeCodes.isEmpty()) {
            transactionFilter.setProcessTypeCodes(null);
        }

        if (transactionFilter.getToDate() != null) {
            transactionFilter.setToDate(
                    Timestamp.valueOf(transactionFilter.getToDate().toLocalDateTime().plusDays(1)));
        }
    }

    /**
     * Logic tính toán SLA Status chung cho cả UI và Excel
     */
    private String calculateSlaStatus(String slaResult,
            Timestamp deadline,
            Long slaMaxDuration,
            String slaWarningType,
            Long slaWarningDuration,
            Long slaWarningPercent) {

        // 1. Ưu tiên check kết quả từ DB (nếu Job đã chạy và đánh dấu)
        if (StatusConstants.BREACHED.equalsIgnoreCase(slaResult)) {
            return StatusConstants.OVER; // Đỏ
        }

        // Nếu không có deadline -> Mặc định Xanh
        if (deadline == null) {
            return StatusConstants.WITHIN_DEADLINE;
        }

        long now = System.currentTimeMillis();
        long deadlineTime = deadline.getTime();

        // 2. Logic Màu Đỏ: Deadline < Hiện tại
        if (deadlineTime < now) {
            return StatusConstants.OVER;
        }

        // 3. Tính khoảng thời gian cảnh báo (Warning Window) tính bằng miliseconds
        long warningWindowMillis = 0;

        if (StatusConstants.FIXED.equalsIgnoreCase(slaWarningType)) {
            // Logic FIXED: Window = SLA_WARNING_DURATION (phút)
            warningWindowMillis = (slaWarningDuration != null ? slaWarningDuration : 0) * 60_000L;

        } else if (StatusConstants.PERCENT.equalsIgnoreCase(slaWarningType)) {
            // Logic PERCENT: Window = SLA_MAX_DURATION * SLA_WARNING_PERCENT
            long maxDurationMillis = (slaMaxDuration != null ? slaMaxDuration : 0) * 60_000L;
            double percent = (slaWarningPercent != null ? slaWarningPercent.doubleValue() : 0.0);

            // Tính ra số millis cảnh báo
            warningWindowMillis = (long) (maxDurationMillis * (percent / 100.0));
        }

        // Thời điểm bắt đầu cảnh báo = Deadline - WarningWindow
        long startWarningTime = deadlineTime - warningWindowMillis;

        // 4. Logic Màu Vàng: Hiện tại >= Thời điểm bắt đầu cảnh báo
        // (Chúng ta đã check màu Đỏ ở trên nên chắc chắn now <= deadline)
        if (now >= startWarningTime) {
            return StatusConstants.APPROACH;
        }

        // 5. Mặc định: Xanh
        return StatusConstants.WITHIN_DEADLINE;
    }

}