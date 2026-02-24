package ngvgroup.com.bpm.features.transaction.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.core.base.repository.BpmTxnProcessInstanceRepository;
import ngvgroup.com.bpm.core.contants.StatusConstants;
import ngvgroup.com.bpm.features.transaction.dto.TransactionInquiryRequestDto;
import ngvgroup.com.bpm.features.transaction.dto.TransactionInquiryResponseDto;
import ngvgroup.com.bpm.features.transaction.service.TransactionInquiryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class TransactionInquiryServiceImpl implements TransactionInquiryService {

    private final BpmTxnProcessInstanceRepository bpmTxnProcessInstanceRepository;

    @Override
    public Page<TransactionInquiryResponseDto> search(String keyword, TransactionInquiryRequestDto dto,
            Pageable pageable) {

        // 1. Xử lý logic ngày tháng (ToDate + 1 ngày để lấy hết ngày đó)
        if (dto.getToDate() != null) {
            dto.setToDate(Timestamp.valueOf(dto.getToDate().toLocalDateTime().plusDays(1)));
        }

        // 2. Xử lý List rỗng
        if (dto.getProcessTypeCodes() != null && dto.getProcessTypeCodes().isEmpty()) {
            dto.setProcessTypeCodes(null);
        }

        // 3. Gọi Repository
        Page<TransactionInquiryResponseDto> page;
        String slaStatus = dto.getSlaStatus();

        if (StatusConstants.OVER.equalsIgnoreCase(slaStatus)) {
            page = bpmTxnProcessInstanceRepository.searchTransactionInquiryOver(keyword, dto, pageable);
        } else if (StatusConstants.WITHIN_DEADLINE.equalsIgnoreCase(slaStatus)
                || StatusConstants.WITHIN.equalsIgnoreCase(slaStatus)) {
            page = bpmTxnProcessInstanceRepository.searchTransactionInquiryWithin(keyword, dto, pageable);
        } else if (StatusConstants.APPROACH.equalsIgnoreCase(slaStatus)) {
            page = bpmTxnProcessInstanceRepository.searchTransactionInquiryApproach(keyword, dto, pageable);
        } else {
            page = bpmTxnProcessInstanceRepository.searchTransactionInquiry(keyword, dto, pageable);
        }

        // 4. Tính toán SLA cho từng bản ghi
        page.forEach(item -> item.setSlaStatus(calculateSlaStatus(item)));

        return page;
    }

    public String calculateSlaStatus(TransactionInquiryResponseDto dto) {
        // 1. Logic: Completed/Cancelled transactions
        // If we can determine a status from the completed state, return it immediately.
        String completedStatus = getCompletedSlaStatus(dto);
        if (completedStatus != null) {
            return completedStatus;
        }

        // 2. Logic: Active transactions (or completed ones falling through)
        long durationMillis = calculateDuration(dto);
        long slaMaxDurationMillis = (dto.getSlaMaxDuration() != null ? dto.getSlaMaxDuration() : 0) * 60_000L;

        // Check strict deadline breach
        if (durationMillis > slaMaxDurationMillis) {
            return StatusConstants.OVER;
        }

        // Check warning thresholds (FIXED vs PERCENT)
        return calculateWarningStatus(dto, durationMillis, slaMaxDurationMillis);
    }

    /**
     * Helper: Calculates duration based on current time and creation date.
     */
    private long calculateDuration(TransactionInquiryResponseDto dto) {
        long currentTime = System.currentTimeMillis();
        long createdTime = dto.getCreatedDate() != null ? dto.getCreatedDate().getTime() : currentTime;
        return currentTime - createdTime;
    }

    /**
     * Helper: Determines status based on SLA Result if the transaction is finished.
     * Returns null if the logic should fall through to time calculation.
     */
    private String getCompletedSlaStatus(TransactionInquiryResponseDto dto) {
        boolean isCompletedOrCancelled = StatusConstants.COMPLETE.equalsIgnoreCase(dto.getBusinessStatus())
                || StatusConstants.CANCEL.equalsIgnoreCase(dto.getBusinessStatus());

        if (isCompletedOrCancelled) {
            if (StatusConstants.BREACHED.equalsIgnoreCase(dto.getSlaResult())) {
                return StatusConstants.OVER;
            }
            if (StatusConstants.ACHIEVED.equalsIgnoreCase(dto.getSlaResult())) {
                return StatusConstants.WITHIN_DEADLINE;
            }
        }
        // Returns null to indicate we should proceed to Logic 2 (Time calculation)
        return null;
    }

    /**
     * Helper: Calculates status based on Warning Types (FIXED or PERCENT).
     */
    private String calculateWarningStatus(TransactionInquiryResponseDto dto, long durationMillis,
            long slaMaxDurationMillis) {
        double warningThresholdMillis;

        if (StatusConstants.FIXED.equalsIgnoreCase(dto.getSlaWarningType())) {
            warningThresholdMillis = (dto.getSlaWarningDuration() != null ? dto.getSlaWarningDuration() : 0) * 60_000L;
        } else if (StatusConstants.PERCENT.equalsIgnoreCase(dto.getSlaWarningType())) {
            double percent = dto.getSlaWarningPercent() != null ? dto.getSlaWarningPercent().doubleValue() : 0.0;
            warningThresholdMillis = slaMaxDurationMillis * (percent / 100.0);
        } else {
            // Default case: No valid warning configuration found
            return StatusConstants.WITHIN_DEADLINE;
        }

        // Common logic: If duration is under the threshold, it is Green; otherwise, it
        // is Approach (Yellow)
        // (We already know it is NOT Over/Red because of the check in the main method)
        return durationMillis < warningThresholdMillis ? StatusConstants.WITHIN_DEADLINE : StatusConstants.APPROACH;
    }
}
