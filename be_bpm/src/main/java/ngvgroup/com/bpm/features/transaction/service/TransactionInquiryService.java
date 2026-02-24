package ngvgroup.com.bpm.features.transaction.service;

import ngvgroup.com.bpm.features.transaction.dto.TransactionInquiryRequestDto;
import ngvgroup.com.bpm.features.transaction.dto.TransactionInquiryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionInquiryService {
    Page<TransactionInquiryResponseDto> search(String keyword, TransactionInquiryRequestDto dto, Pageable pageable);
}
