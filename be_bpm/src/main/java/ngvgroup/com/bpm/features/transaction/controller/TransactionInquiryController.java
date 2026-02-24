package ngvgroup.com.bpm.features.transaction.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.features.transaction.dto.TransactionInquiryRequestDto;
import ngvgroup.com.bpm.features.transaction.dto.TransactionInquiryResponseDto;
import ngvgroup.com.bpm.features.transaction.service.TransactionInquiryService;

@RestController
@RequestMapping("/transaction-inquiry")
@RequiredArgsConstructor
public class TransactionInquiryController {
    private final TransactionInquiryService service;
    private final ExportExcel exportExcel;
    @PostMapping("")
    ResponseEntity<ResponseData<Page<TransactionInquiryResponseDto>>> search(@RequestParam String keyword,
            @RequestBody TransactionInquiryRequestDto dto, Pageable pageable) {
        return ResponseData.okEntity(service.search(keyword, dto, pageable));
    }

    @PostMapping("/export-to-excel")
    ResponseEntity<byte[]> exportExcel(@RequestParam(defaultValue = "all") String type,
            @RequestBody TransactionInquiryRequestDto dto, Pageable pageable) {
        if("all".equalsIgnoreCase(type)) {
            pageable = Pageable.unpaged();
        }
        try {
            return exportExcel.exportExcel(service.search(null, dto, pageable).getContent(), "Tìm kiếm giao dịch");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Export excel thất bại");
        }
    }
}
