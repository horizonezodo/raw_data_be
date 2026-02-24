package ngvgroup.com.bpm.features.transaction.service;
import ngvgroup.com.bpm.features.transaction.dto.TransactionDto;
import ngvgroup.com.bpm.features.transaction.filter.TransactionFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
public interface TransactionOutboxService {
    Page<TransactionDto> getOutbox(String keyword,TransactionFilter transactionDto, Pageable pageable);

    ResponseEntity<byte[]> exportToExcel(TransactionFilter transactionDto, String fileName);
}
