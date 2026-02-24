package com.naas.admin_service.features.mail.controller;

import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateRequest;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse;
import com.naas.admin_service.features.mail.service.ComCfgMailTemplateService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/com-cfg-mail-template")
@Slf4j
@PreAuthorize("hasRole('admin_email_template')")
public class ComCfgMailTemplateController {

    private final ComCfgMailTemplateService comCfgMailTemplateService;

    @Operation(
            summary = "Lấy danh sách mail template",
            description = "API dùng để lấy danh sách các mẫu mail trong hệ thống",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy mẫu mail")
            }
    )
    @PostMapping("/get-all")
    public ResponseEntity<ResponseData<PageResponse<ComCfgMailTemplateResponse>>> getAll(@RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(comCfgMailTemplateService.getAll(request));
    }

    @Operation(
            summary = "Lấy thông tin mail template",
            description = "API dùng để lấy thông tin của một mẫu mail dựa trên mã template",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy mã template")
            }
    )
    @GetMapping("/get-detail/{mailTemplateCode}")
    public ResponseEntity<ResponseData<ComCfgMailTemplateResponse>> getDetail(
            @PathVariable String mailTemplateCode
    ) {
        ComCfgMailTemplateResponse comCfgMailTemplateResponse = comCfgMailTemplateService.getDetail(mailTemplateCode);
        return ResponseData.okEntity(comCfgMailTemplateResponse);
    }

    @Operation(
            summary = "Tạo mới mail template",
            description = "API dùng để tạo mới mẫu mail gửi đi trong hệ thống",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tạo thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
                    @ApiResponse(responseCode = "409", description = "Mã template đã tồn tại")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createComCfgMailTemplate(
            @Valid @RequestBody ComCfgMailTemplateRequest comCfgMailTemplateRequest
    )  {
        comCfgMailTemplateService.createComCfgMailTemplate(comCfgMailTemplateRequest);
        return ResponseData.createdEntity();
    }

    @Operation(
            summary = "Cập nhật mail template",
            description = "Cập nhật thông tin của một mẫu mail dựa trên mã template",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy mã template"),
                    @ApiResponse(responseCode = "409", description = "Mã template mới đã tồn tại")
            }
    )
    @PutMapping("/update/{mailTemplateCode}")
    public ResponseEntity<ResponseData<Void>> updateComCfgMail(
            @PathVariable String mailTemplateCode,
            @Valid @RequestBody ComCfgMailTemplateRequest comCfgMailTemplateRequest
    )  {
        comCfgMailTemplateService.updateComCfgMail(mailTemplateCode, comCfgMailTemplateRequest);
        return ResponseData.okEntity(null);
    }

    @Operation(
            summary = "Xóa mail template",
            description = "Xóa một mẫu mail dựa trên mã template",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Xóa thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy mã template")
            }
    )
    @DeleteMapping("/delete/{mailTemplateCode}")
    public ResponseEntity<ResponseData<Void>> deleteComCfgMail(
            @PathVariable String mailTemplateCode
    )  {
        comCfgMailTemplateService.deleteComCfgMail(mailTemplateCode);
        return ResponseData.noContentEntity();
    }

    @Operation(
            summary = "Export cấu hình mail gửi",
            description = "API dùng để tải xuống cấu hình mail gửi",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Download thành công"),
            }
    )
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable("fileName") String fileName,
                                                @RequestBody ExportExcelRequest request) {
        return comCfgMailTemplateService.exportExcel(fileName, request);
    }
}

