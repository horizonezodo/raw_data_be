package com.naas.admin_service.features.mail.controller;

import com.naas.admin_service.core.kafka.dto.MailProcessRequestDto;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailDto;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailResponse;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailSearchRequest;
import com.naas.admin_service.features.mail.service.MailService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/com-log-mail")
public class ComLogMailController {
    private final MailService mailService;

    public ComLogMailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send-mail")
    public void createComLogMail(@RequestBody MailProcessRequestDto dto) {
        mailService.sendMail(dto);
    }

    @Operation(
            summary = "Tìm kiếm mail log",
            description = "API dùng để tìm kiếm lịch sử gửi mail",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tìm kiếm thành công"),
            }
    )
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<ComLogMailResponse>>> searchMail(@RequestBody ComLogMailSearchRequest request) {
        return ResponseData.okEntity(mailService.searchLogMail(request));
    }

    @Operation(
            summary = "Download excel mail log",
            description = "API dùng để tải xuống lịch sử gửi mail",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Download thành công"),
            }
    )
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable("fileName") String fileName,
                                                @RequestBody ComLogMailSearchRequest request) {
        return mailService.downloadLogMail(fileName, request);
    }


    @Operation(
            summary = "Lấy thông tin mail log theo id",
            description = "API dùng để lấy thông tin lịch sử gửi mail theo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin mail log"),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ComLogMailDto>> getComLogMailById(@PathVariable Long id) {
        return ResponseData.okEntity(mailService.getLogMailDetail(id));
    }
}
