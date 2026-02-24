package ngvgroup.com.bpm.features.transaction.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.transaction.dto.TransactionDto;
import ngvgroup.com.bpm.features.transaction.filter.TransactionFilter;
import ngvgroup.com.bpm.features.transaction.service.TransactionOutboxService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction-outbox")
@AllArgsConstructor
public class TransactionOutboxController {
    private final TransactionOutboxService transactionOutboxService;
    @PostMapping
    ResponseEntity<ResponseData<Page<TransactionDto>>> getOutbox(@RequestParam String keyword, @RequestBody TransactionFilter transactionFilter, Pageable pageable){
        return ResponseData.okEntity(transactionOutboxService.getOutbox(keyword,transactionFilter,pageable));
    }

    @PostMapping("/export-to-excel-outbox")
    ResponseEntity<byte[]> exportToExcel(@RequestBody TransactionFilter transactionDto, @RequestParam String fileName){
        return transactionOutboxService.exportToExcel(transactionDto,fileName);
    }
}
