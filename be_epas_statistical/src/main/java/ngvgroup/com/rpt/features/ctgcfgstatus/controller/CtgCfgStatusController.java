package ngvgroup.com.rpt.features.ctgcfgstatus.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusDto;
import ngvgroup.com.rpt.features.ctgcfgstatus.service.CtgCfgStatusService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/status")
//@NoAuditLog
public class CtgCfgStatusController {
    private final CtgCfgStatusService service;

    @LogActivity(function = "Lấy danh sách trạng thái")
    @Operation(summary = "Lấy danh sách trạng thái")
    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgStatusDto>>> pageStatus(@RequestParam String keyword,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(service.pageStatus(keyword, pageable));
    }

    @LogActivity(function = "Lấy tất cả trạng thái")
    @Operation(summary = "Lấy tất cả trạng thái")
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgCfgStatusDto>>> getAllStatusByType(@RequestParam(required = false) String type) {
        return ResponseData.okEntity(service.getAllStatusByType(type));
    }

    @LogActivity(function = "Lấy chi tiết trạng thái")
    @Operation(summary = "Lấy thông tin chi tiết trạng thái")
    @GetMapping("/get/{statusCode}")
    public ResponseEntity<ResponseData<CtgCfgStatusDto>> getDetail(@PathVariable("statusCode") String statusCode) {
        return ResponseData.okEntity(service.getDetail(statusCode));
    }

    @LogActivity(function = "Thêm trạng thái")
    @Operation(summary = "Thêm trạng thái")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createStatus(@RequestBody CtgCfgStatusDto dto) {
        service.createStatus(dto);
        return ResponseData.createdEntity();
    }

    @LogActivity(function = "Sửa trạng thái")
    @Operation(summary = "Sửa trạng thái")
    @PostMapping("/update/{statusCode}")
    public ResponseEntity<ResponseData<Void>> updateStatus(@RequestBody CtgCfgStatusDto dto,
            @PathVariable("statusCode") String statusCode) {
        service.updateStatus(dto, statusCode);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Xóa trạng thái")
    @Operation(summary = "Xóa trạng thái")
    @PostMapping("/delete/{statusCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable("statusCode") String statusCode) {
        service.deleteStatus(statusCode);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Xuất danh sách trạng thái")
    @Operation(summary = "Xuất danh sách trạng thái")
    @PostMapping("/export-to-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam List<String> labels,
            @RequestParam String keyword,
            @RequestParam String fileName) {
        return service.exportToExcel(labels, keyword, fileName);
    }

    @LogActivity(function = "Lấy tất cả mã trạng thái")
    @GetMapping("/get-all-code")
    public ResponseEntity<ResponseData<List<String>>> getAllCode(){
        return ResponseData.okEntity(service.getAllCode());
    }
}
