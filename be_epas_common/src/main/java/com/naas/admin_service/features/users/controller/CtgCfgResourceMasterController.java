package com.naas.admin_service.features.users.controller;

import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDto;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.ExportExcelReq;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.SearchDTO;
import com.naas.admin_service.features.users.service.CtgCfgResourceMasterService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@LogActivity(function = "Quản lý loại tài nguyên")
@RestController
@RequestMapping("/com-cfg-resource-master")
@PreAuthorize("hasRole('admin_resource_type')")
public class CtgCfgResourceMasterController {

        private final CtgCfgResourceMasterService ctgCfgResourceMasterService;

        public CtgCfgResourceMasterController(CtgCfgResourceMasterService ctgCfgResourceMasterService) {
                this.ctgCfgResourceMasterService = ctgCfgResourceMasterService;
        }

        @Operation(summary = "Tìm kiếm thông loại tin tài nguyên", description = "Tìm kiếm thông tin loại tài nguyên theo mã loại tài nguyên hoặc tên loại tài nguyên, phân trang và tìm kiếm từ DTO", responses = {
                        @ApiResponse(responseCode = "200", description = "Thành công"),
                        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
        })
        @PostMapping("/search")
        @LogActivity(function = "Tìm kiếm loại tài nguyên")
        public ResponseEntity<ResponseData<Page<CtgCfgResourceMasterDto>>> searchComCfgResourceMasterService(
                        @RequestBody SearchDTO searchDTO) {
                return ResponseData.okEntity(ctgCfgResourceMasterService.searchCfgResourceMaster(searchDTO));
        }

        @Operation(summary = "Lấy thông tin loại tài nguyên ", description = "Lấy thông tin loại tài nguyên theo id", responses = {
                        @ApiResponse(responseCode = "200", description = "Thành công"),
                        @ApiResponse(responseCode = "404", description = "Tài nguyên không tồn tại")
        })
        @GetMapping("/{id}")
        @LogActivity(function = "Lấy thông tin loại tài nguyên")
        public ResponseEntity<ResponseData<CtgCfgResourceMasterDto>> findComCfgResourceMasterServiceById(
                        @PathVariable("id") long id) {
                return ResponseData.okEntity(ctgCfgResourceMasterService.getCfgResourceMasterById(id));
        }

        @Operation(summary = "Tạo loại tài nguyên", description = "Tạo loại tài nguyên", responses = {
                        @ApiResponse(responseCode = "200", description = "Thành công"),
                        @ApiResponse(responseCode = "404", description = "Dữ liệu không hợp lệ")
        })
        @PostMapping("/create")
        @LogActivity(function = "Tạo loại tài nguyên")
        public ResponseEntity<ResponseData<Void>> createComCfgResourceMaster(
                        @RequestBody CtgCfgResourceMasterDto ctgCfgResourceMasterDto) {
                ctgCfgResourceMasterService.createCfgResourceMaster(ctgCfgResourceMasterDto);
                return ResponseData.createdEntity();
        }

        @Operation(summary = "Cập nhật thông tin loại tài nguyên", description = "Cập nhật thông tin loại tài nguyên", responses = {
                        @ApiResponse(responseCode = "200", description = "Thành công"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy tài nguyên")
        })
        @PutMapping("/update/{id}")
        @LogActivity(function = "Cập nhật loại tài nguyên")
        public ResponseEntity<ResponseData<Void>> updateComCfgResourceMaster(
                        @RequestBody CtgCfgResourceMasterDto ctgCfgResourceMasterDto,
                        @PathVariable("id") long id) {
                ctgCfgResourceMasterService.updateCfgResourceMaster(id, ctgCfgResourceMasterDto);
                return ResponseData.noContentEntity();
        }

        @Operation(summary = "Xóa loại tài nguyên", description = "Xóa loại tài nguyên theo id", responses = {
                        @ApiResponse(responseCode = "200", description = "Thành công"),
                        @ApiResponse(responseCode = "404", description = "Dữ liệu không hợp lệ"),
                        @ApiResponse(responseCode = "400", description = "Loại tài nguyên đã được mapping")
        })
        @DeleteMapping("/delete/{id}")
        @LogActivity(function = "Xóa loại tài nguyên")
        public ResponseEntity<ResponseData<Void>> deleteComCfgResourceMasterServiceById(@PathVariable("id") long id) {
                ctgCfgResourceMasterService.deleteCfgResourceMasterById(id);
                return ResponseData.noContentEntity();
        }

        @Operation(summary = "Api export danh sách loại tài nguyên", description = "Export danh sách loại tài nguyên", responses = {
                        @ApiResponse(responseCode = "200", description = "Thành công"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy loại tài nguyên "),
        })
        @PostMapping("/export-excel")
        @LogActivity(function = "Xuất Excel danh sách loại tài nguyên")
        public ResponseEntity<ByteArrayResource> exportExcel(@RequestBody ExportExcelReq request) {
                return ctgCfgResourceMasterService.exportExcel(request);
        }
}
