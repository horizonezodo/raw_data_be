package com.naas.admin_service.features.mail.controller;

import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromDto;
import com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromResponse;
import com.naas.admin_service.features.mail.service.ComCfgMailFromService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/com-cfg-mail-from")
public class ComCfgMailFromController {
    private final ComCfgMailFromService cfgMailFromService;

    public ComCfgMailFromController(ComCfgMailFromService cfgMailFromService) {
        this.cfgMailFromService = cfgMailFromService;
    }

    @Operation(
            summary = "Tạo mail gửi",
            description = "API dùng để tạo thông tin mail gửi",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "500", description = "Lỗi encrypt mật khẩu"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy template mail"),
                    @ApiResponse(responseCode = "400", description = "Template mail đã được sử dụng"),
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createComCfgMailFrom(@RequestBody ComCfgMailFromDto comCfgMailFromDto) {
        cfgMailFromService.createComCfgMailFrom(comCfgMailFromDto);
        return ResponseData.createdEntity();
    }

    @Operation(
            summary = "Cập nhật mail gửi",
            description = "API dùng để cập nhật thông tin mail gửi",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "500", description = "Lỗi encrypt mật khẩu"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy template mail/Mail gửi"),
                    @ApiResponse(responseCode = "400", description = "Template mail đã được sử dụng"),
            }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseData<Void>> updateComCfgMailFrom(
            @PathVariable("id") long id,
            @RequestBody ComCfgMailFromDto comCfgMailFromDto
    ) {
        cfgMailFromService.updateComCfgMailFrom(id, comCfgMailFromDto);
        return ResponseData.noContentEntity();
    }

    @Operation(
            summary = "Tìm kiếm mail gửi",
            description = "API dùng để tìm kiếm thông tin mail gửi",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
            }
    )
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<ComCfgMailFromResponse>>> searchComCfgMailFrom(@RequestBody SearchFilterRequest searchFilterRequest) {
        return ResponseData.okEntity(cfgMailFromService.searchConCfgMailFrom(searchFilterRequest));
    }

    @Operation(
            summary = "Lấy thông tin mail gửi theo id",
            description = "API dùng để lấy thông tin chi tiết mail gửi",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin mail gửi"),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ComCfgMailFromDto>> getDetailComCfgMailFrom(@PathVariable("id") long id) {
        return ResponseData.okEntity(cfgMailFromService.findComCfgMailFromById(id));
    }

    @Operation(
            summary = "Xóa thông tin mail gửi",
            description = "API dùng để xóa mail gửi",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin mail gửi"),
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<Void>> deleteComCfgMailFrom(@PathVariable("id") long id) {
        cfgMailFromService.deleteComCfgMailFromById(id);
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
        return cfgMailFromService.exportExcel(fileName, request);
    }
}
