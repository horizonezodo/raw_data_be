package com.naas.admin_service.features.setting.controller;

import com.naas.admin_service.features.setting.dto.ComCfgParameterDto;
import com.naas.admin_service.features.setting.dto.ComCfgParameterResponse;
import com.naas.admin_service.features.setting.service.ComCfgParameterService;
import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/com-cfg-parameter")
public class ComCfgParameterController {
    private final ComCfgParameterService service;

    @Operation(
            summary = "Tạo mới tham số hệ thống",
            description = "API dùng để tạo mới tham số hệ thống",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tạo mới thành công"),
                    @ApiResponse(responseCode = "409", description = "Mã tham số đã tồn tại trong hệ thống")
            }
    )
    @PostMapping
    public ResponseEntity<ResponseData<Void>> createComCfgParameter(@RequestBody ComCfgParameterDto dto) {
        service.createComCfgParameter(dto);
        return ResponseData.createdEntity();
    }

    @Operation(
            summary = "Cập nhật tham số hệ thống",
            description = "API dùng để cập nhật tham số hệ thống",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm tham số hệ thống")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> updateComCfgParameter(@PathVariable Long id, @RequestBody ComCfgParameterDto dto) {
        service.updateComCfgParameter(id, dto);
        return ResponseData.noContentEntity();
    }

    @Operation(
            summary = "Xem chi tiết tham số hệ thống",
            description = "API dùng để xem chi tiết tham số hệ thống",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trả thông tin tham số hệ thống"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy tham số hệ thống")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ComCfgParameterDto>> getComCfgParameter(@PathVariable Long id) {
        return ResponseData.okEntity(service.findComCfgParameterById(id));
    }

    @Operation(
            summary = "Lấy thông tin tham số theo mã tham số",
            description = "API dùng để lấy thông tin tham số hệ thống theo mã tham số",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trả thông tin tham số hệ thống"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy tham số hệ thống theo mã tham số")
            }
    )
    @GetMapping("/param-code/{paramCode}")
    public ResponseEntity<ResponseData<ComCfgParameterDto>> getComCfgParameterByCode(@PathVariable String paramCode) {
        return ResponseData.okEntity(service.findComCfgParameterByCode(paramCode));
    }

    @Operation(
            summary = "Xóa tham số hệ thống",
            description = "API dùng để xóa tham số hệ thống",
            responses = {
                    @ApiResponse(responseCode = "200", description = "xóa thành công tham số hệ thống"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy tham số hệ thống")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteComCfgParameter(@PathVariable Long id) {
        service.deleteComCfgParameter(id);
        return ResponseData.noContentEntity();
    }

    @Operation(
            summary = "Tìm kiếm tham số hệ thống",
            description = "API dùng để tìm kiếm tham số hệ thống",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trả danh sách tham số hệ thống"),
            }
    )

    @PostMapping("/search")
    public ResponseEntity<ResponseData<List<ComCfgParameterResponse>>> searchComCfgParameter(@RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(service.listComCfgParameter(request));
    }

    @Operation(
            summary = "Xuất danh sách tham số hệ thống",
            description = "API dùng để xuất danh sách tham số hệ thống",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy tham số hệ thống")
            }
    )
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportComCfgParameterExcel(@RequestBody ExportExcelRequest request) {
        return service.exportExcel(request);
    }

    @Operation(
            summary = "Xuất danh sách tham số hệ thống dựa theo paramCode và paramValue",
            description = "API dùng để xuất danh sách tham số hệ thống dựa theo paramCode và paramValue",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy tham số hệ thống")
            }
    )
    @GetMapping("/get-list")
    public ResponseEntity<ResponseData<List<ComCfgParameterDto>>> getAllByCodeAndValue(@RequestParam("paramCode")String paramCode, @RequestParam("paramValue")String paramValue){
        return ResponseData.okEntity(service.getAllByCodeAndValue(paramCode,paramValue));
    }
}
