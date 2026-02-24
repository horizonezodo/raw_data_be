package com.naas.admin_service.features.category.controller;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.category.dto.ReportReqDto;
import com.naas.admin_service.features.category.dto.TemplateReqDto;
import com.naas.admin_service.features.category.dto.TemplateReqExcelDto;
import com.naas.admin_service.features.category.dto.TemplateResDto;
import com.naas.admin_service.features.category.service.ComCfgTemplateService;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/template")
public class ComCfgTemplateController {
    private final ComCfgTemplateService comCfgTemplateService;
    private final ExportExcel exportExcel;

    public ComCfgTemplateController(ComCfgTemplateService comCfgTemplateService, ExportExcel exportExcel) {
        this.comCfgTemplateService = comCfgTemplateService;
        this.exportExcel = exportExcel;
    }

    @Operation(summary = "Lấy danh sách template file (active = 1)",
            description = """
            Trả về danh sách template có trạng thái active = 1, hỗ trợ tìm kiếm theo từ khóa và phân trang.
            - keyword: Từ khóa tìm kiếm theo tên/miêu tả
            - pageable: thông tin phân trang (page, size, sort)
            """)
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<PageResponse<TemplateResDto>>> getAll(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<TemplateResDto> page = comCfgTemplateService.searchListTemplate(keyword, pageable);
        PageResponse<TemplateResDto> response = new PageResponse<>(page);
        return ResponseData.okEntity(response);
    }

    @Operation(summary = "Tạo mới template",
            description = """
            Tạo mới một template và upload file đi kèm lên MinIO.
            - data: Thông tin template (JSON)
            - file: File đính kèm (multipart/form-data)
            - TemplateCode phải là duy nhất
            """)
    @PostMapping("/create")
    public ResponseEntity<ResponseData<TemplateResDto>> createTemplate(
            @RequestPart("data") TemplateReqDto dto,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "fileMappingPath", required = false) MultipartFile fileMapping) {
        if (comCfgTemplateService.existsByTemplateCode(dto.getTemplateCode())) {
            throw new BusinessException(CommonErrorCode.EXISTS);
        }
        TemplateResDto result = comCfgTemplateService.createTemplate(dto, file, fileMapping);
        return ResponseData.okEntity(result);
    }

    @Operation(summary = "Cập nhật template",
            description = """
            Cập nhật thông tin và/hoặc file của template theo mã templateCode.
            - Nếu có file mới, sẽ thay thế file cũ trong MinIO
            - Các trường không truyền sẽ giữ nguyên
            """)
    @PutMapping("/update/{templateCode}")
    public ResponseEntity<ResponseData<TemplateResDto>> updateTemplate(
            @PathVariable String templateCode,
            @RequestPart("data") TemplateReqDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "fileMappingPath",required = false) MultipartFile fileMappingPath) {
        TemplateResDto updatedDto = comCfgTemplateService.updateTemplate(templateCode, dto, file, fileMappingPath);
        if (updatedDto == null) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }
        return ResponseData.okEntity(updatedDto);
    }

    @Operation(summary = "Xóa template theo templateCode", description = "Xóa template theo templateCode (xóa mềm)", responses = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy template")
    })
    @DeleteMapping("/delete/{templateCode}")
    public ResponseEntity<ResponseData<Void>> deleteTemplate(@PathVariable String templateCode) {
        if (!comCfgTemplateService.existsByTemplateCode(templateCode)) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }
        comCfgTemplateService.deleteTemplate(templateCode);
        return ResponseData.okEntity(null);
    }

    @Operation(summary = "Lấy chi tiết template", description = "Lấy chi tiết template theo templateCode", responses = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy template")
    })
    @GetMapping("/get-detail/{templateCode}")
    public ResponseEntity<ResponseData<TemplateResDto>> getDetail(
            @PathVariable String templateCode) {
        TemplateResDto dto = comCfgTemplateService
                .getDetail(templateCode);
        if (dto == null) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }
        return ResponseData.okEntity(dto);
    }

    @Operation(summary = "Lấy chi tiết template + file", description = "Lấy chi tiết template theo templateCode kèm theo dữ liệu file từ minio", responses = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy template")
    })
    @GetMapping("/get-detail-with-file/{templateCode}")
    public ResponseEntity<ResponseData<TemplateResDto>> getDetailWithFile(
            @PathVariable String templateCode) {
        TemplateResDto dto = comCfgTemplateService
                .getDetailWithFile(templateCode);
        if (dto == null) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }
        return ResponseData.okEntity(dto);
    }

    @Operation(summary = "Download file", description = "Tải file từ MinIO theo templateCode. Header response sẽ bao gồm Content-Disposition để chỉ định tên file.\n"
            +
            "Frontend cần xử lý để tự động tải file.")
    @GetMapping("/download-file/{templateCode}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String templateCode) {
        byte[] fileData = comCfgTemplateService.downloadFileTemplate(templateCode);
        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }
        // Lấy filePath để làm tên file trong header
        String filePath = comCfgTemplateService.getDetail(templateCode).getFilePath();
        return ResponseEntity.ok()
                .header(Constant.CONTENT_DISPOSITION, Constant.ATTACHMENT + filePath + "\"")
                .header(Constant.ACCESS_CONTROL_EXPOSE_HEADERS, Constant.CONTENT_DISPOSITION)
                .body(fileData);
    }

    @Operation(summary = "Download file Mapping", description = "Tải file từ MinIO theo templateCode. Header response sẽ bao gồm Content-Disposition để chỉ định tên file.\n"
            +
            "Frontend cần xử lý để tự động tải file.")
    @GetMapping("/download-file-mapping/{templateCode}")
    public ResponseEntity<byte[]> downloadFileMapping(@PathVariable String templateCode) {
        byte[] fileData = comCfgTemplateService.downloadFileMappingTemplate(templateCode);
        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }
        // Lấy filePath để làm tên file trong header
        String filePath = comCfgTemplateService.getDetail(templateCode).getFileMappingPath();
        return ResponseEntity.ok()
                .header(Constant.CONTENT_DISPOSITION, Constant.ATTACHMENT + filePath + "\"")
                .header(Constant.ACCESS_CONTROL_EXPOSE_HEADERS, Constant.CONTENT_DISPOSITION)
                .body(fileData);
    }

    @Operation(summary = "Xóa file khỏi MinIO", description = "Xóa file đính kèm khỏi MinIO của template tương ứng và cập nhật lại database (xóa đường dẫn và size).")
    @DeleteMapping("/remove-file/{templateCode}")
    public ResponseEntity<ResponseData<Void>> removeFile(@PathVariable String templateCode) {
        if (!comCfgTemplateService.existsByTemplateCode(templateCode)) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }
        comCfgTemplateService.removeFile(templateCode);
        return ResponseData.okEntity(null);
    }

    @Operation(summary = "Xóa file khỏi MinIO", description = "Xóa file đính kèm khỏi MinIO của template tương ứng và cập nhật lại database (xóa đường dẫn và size).")
    @DeleteMapping("/remove-file-mapping/{templateCode}")
    public ResponseEntity<ResponseData<Void>> removeFileMapping(@PathVariable String templateCode) {
        if (!comCfgTemplateService.existsByTemplateCode(templateCode)) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }
        comCfgTemplateService.removeFileMapping(templateCode);
        return ResponseData.okEntity(null);
    }

    @Operation(summary = "Xuất file excel", description = "Tạo file Excel chứa các giá trị từ bảng template", responses = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "500", description = "Lỗi xử lý nội bộ")
    })
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable String fileName, @RequestBody TemplateReqExcelDto req) throws Exception {
        return exportExcel.exportExcel(comCfgTemplateService.generateExcelFile(req), fileName);
    }

    @Operation(summary = "Gen report", description = "Truyền request body gồm templateCode, format và dataSource (optional). Nếu có dataSource thì dùng trực tiếp, ngược lại lấy mapping từ MinIO")
    @PostMapping({"/generate-report"})
    public ResponseEntity<byte[]> generateReport(@Valid @RequestBody ReportReqDto request) {
        byte[] reportData = comCfgTemplateService.generateReport(request);
        String reportName = "report." + Constant.resolveReportExtension(request.getFormat());
        MediaType mediaType = Constant.resolveReportMediaType(request.getFormat());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(Constant.CONTENT_DISPOSITION, Constant.ATTACHMENT + reportName + "\"")
                .header(Constant.ACCESS_CONTROL_EXPOSE_HEADERS, Constant.CONTENT_DISPOSITION)
                .body(reportData);
    }
}
