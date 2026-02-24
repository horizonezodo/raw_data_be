package com.naas.admin_service.features.setting.controller;

import com.naas.admin_service.features.setting.dto.AppConfigResponseDto;
import com.naas.admin_service.features.setting.dto.ComCfgSettingReqDto;
import com.naas.admin_service.features.setting.dto.ReportDto;
import com.naas.admin_service.features.setting.service.ComCfgSettingService;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/setting")
@RequiredArgsConstructor

public class ComCfgSettingController {

    private final ComCfgSettingService service;

    @Operation(summary = "Tạo hoặc cập nhật danh sách cấu hình", description = "API nhận mảng các cấu hình, thêm mới nếu chưa tồn tại hoặc cập nhật nếu đã tồn tại")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @PostMapping
    @PreAuthorize("hasRole('admin_setting_common')")
    public ResponseEntity<ResponseData<Void>> createOrUpdateSettings(
            @Valid @RequestBody List<ComCfgSettingReqDto> requests) {
        service.createOrUpdateSettings(requests);
        return ResponseData.okEntity(null);
    }

    @Operation(summary = "Lấy danh sách cấu hình theo danh sách mã cấu hình", description = "API nhận danh sách mã cấu hình, trả về danh sách cấu hình tương ứng")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping
    public ResponseEntity<ResponseData<List<ComCfgSettingReqDto>>> getSettingsByCodes(
            @RequestParam List<String> settingCodes) {
        List<ComCfgSettingReqDto> settings = service.getSettingsByCodes(settingCodes);
        return ResponseData.okEntity(settings);
    }

    @Operation(summary = "Lấy danh sách dashboard phân trang", description = "API trả về danh sách dashboard/URL và tên dashboard từ CTG_COM_COMMON (CM056) và CTG_CFG_REPORT (CM036.001)")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping("/dashboard-list")
    public ResponseEntity<ResponseData<PageResponse<ReportDto>>> getDashboardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ReportDto> result = service.getDashboardList(pageable);
        return ResponseData.okEntity(result);
    }

    @Operation(summary = "Xóa setting theo mã code", description = "API xóa setting, nếu là ảnh thì xóa cả file trên MinIO")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> deleteSetting(@RequestParam String settingCode) {
        service.deleteSetting(settingCode);
        return ResponseData.noContentEntity();
    }

    @Operation(summary = "Lấy cấu hình bât tắt Log")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping("/get-config-log")
    public ResponseEntity<ResponseData<Map<String, Object>>> getConfigLog(@RequestParam(required = false) String serviceName ) {
        Map<String, Object> result = service.getConfigLog();
        return ResponseData.okEntity(result);
    }

    @Operation(summary = "Lấy toàn bộ cấu hình app", description = "API trả về cấu hình layout, captcha, multitenancy trong 1 lần gọi")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping("/app-config")
    public ResponseEntity<ResponseData<AppConfigResponseDto>> getAppConfig() {
        AppConfigResponseDto config = service.getAppConfig();
        return ResponseData.okEntity(config);
    }

}
