package ngvgroup.com.bpm.features.transaction.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.transaction.dto.TransactionDto;
import ngvgroup.com.bpm.features.transaction.filter.TransactionFilter;
import ngvgroup.com.bpm.features.transaction.service.TransactionInboxService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction-inbox")
@AllArgsConstructor
public class TransactionInboxController {

    private final TransactionInboxService transactionInboxService;
    @PostMapping
    ResponseEntity<ResponseData<Page<TransactionDto>>> getTaskInbox(@RequestParam String keyword,@RequestBody TransactionFilter transactionFilter, Pageable pageable){
        return ResponseData.okEntity(transactionInboxService.getTaskInbox(keyword,transactionFilter,pageable));
    }

    @PostMapping("/export-to-excel-inbox")
    ResponseEntity<byte[]> exportToExcel(@RequestBody TransactionFilter transactionDto,@RequestParam String fileName){
        return transactionInboxService.exportToExcel(transactionDto,fileName);
    }

}
