package ngvgroup.com.rpt.features.report.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup.CtgCfgReportGroupDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroupdto.CtgCfgReportGroupDTO;
import ngvgroup.com.rpt.features.report.dto.SearchFilterRequest;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportGroupService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report-group")
public class CtgCfgReportGroupController {

    private final CtgCfgReportGroupService ctgCfgReportGroupService;

    public CtgCfgReportGroupController(CtgCfgReportGroupService ctgCfgReportGroupService) {
        this.ctgCfgReportGroupService = ctgCfgReportGroupService;
    }

    @LogActivity(function = "Lấy danh sách nhóm báo cáo")
    @GetMapping("/get")
    public ResponseEntity<ResponseData<Page<CtgCfgReportGroupDto>>> getListReportGroup(@ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgCfgReportGroupService.getListReportGroups(pageable));
    }

    @LogActivity(function = "Tìm kiếm nhóm báo cáo")
    @PostMapping("/find-list")
    public ResponseEntity<ResponseData<Page<CtgCfgReportGroupDto>>>findListReportGroup(@RequestBody SearchFilterRequest searchFilterRequest) {
        return ResponseData.okEntity(ctgCfgReportGroupService.findListReportGroups(searchFilterRequest));
    }

    @LogActivity(function = "Xuất Excel nhóm báo cáo")
    @PostMapping("/export-to-excel/{fileName}")
    public ResponseEntity<byte[]>exportToExcel(@RequestBody CtgCfgReportGroupDto ctgCfgReportGroupDto, @PathVariable("fileName") String fileName) {
        return ctgCfgReportGroupService.exportToExcel(ctgCfgReportGroupDto, fileName);
    }

    @LogActivity(function = "Tạo nhóm báo cáo")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createReportGroup(@RequestBody CtgCfgReportGroupDto ctgCfgReportGroupDto)  {
        ctgCfgReportGroupService.createReportGroup(ctgCfgReportGroupDto);
        return ResponseData.createdEntity();
    }

    @LogActivity(function = "Cập nhật nhóm báo cáo")
    @PutMapping("/update")
    public ResponseEntity<ResponseData<Void>> updateReportGroup(@RequestBody CtgCfgReportGroupDto ctgCfgReportGroupDto)  {
        ctgCfgReportGroupService.updateReportGroup(ctgCfgReportGroupDto);
        return ResponseData.createdEntity();
    }

    @LogActivity(function = "Lấy chi tiết nhóm báo cáo")
    @GetMapping("/get-info/{reportGroupCode}")
    public ResponseEntity<ResponseData<CtgCfgReportGroupDto>> getInfoReportGroup(@PathVariable("reportGroupCode") String reportGroupCode) {
        return ResponseData.okEntity(ctgCfgReportGroupService.getInfoReportGroup(reportGroupCode));
    }

    @LogActivity(function = "Xóa nhóm báo cáo")
    @DeleteMapping("/delete/{reportGroupCode}")
    public ResponseEntity<ResponseData<Void>> deleteReportGroup(@PathVariable("reportGroupCode") String reportGroupCode)  {
        ctgCfgReportGroupService.deleteReportGroup(reportGroupCode);
        return ResponseData.createdEntity();
    }

    @LogActivity(function = "Lấy tất cả nhóm báo cáo")
    @Operation(summary = "Lấy danh sách report group", description = "Trả về danh sách report group")
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgCfgReportGroupDTO>>> getAll() {
        List<CtgCfgReportGroupDTO> ctgCfgReportGroupDTOS = ctgCfgReportGroupService.getAll();
        return ResponseData.okEntity(ctgCfgReportGroupDTOS);
    }

    @LogActivity(function = "Lấy danh sách nhóm báo cáo")
    @GetMapping("/get-list")
    public ResponseEntity<ResponseData<List<CtgCfgReportGroupDto>>>getList(){
        return ResponseData.okEntity(ctgCfgReportGroupService.getListReportGroups());
    }

    @LogActivity(function = "Kiểm tra nhóm báo cáo tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String reportGroupCode) {
        return ResponseData.okEntity(ctgCfgReportGroupService.checkExist(reportGroupCode));
    }
}
