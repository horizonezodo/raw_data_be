package ngvgroup.com.rpt.features.report.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.CtgCfgReportParamDTO;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportMiningParamDTO;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ResourceParamDTO;
import ngvgroup.com.rpt.features.report.helper.PageResponse;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportParamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report-param")
public class CtgCfgReportParamController {
    private final CtgCfgReportParamService ctgCfgReportParamService;

    @Operation(
            summary = "Lấy danh sách tham số đầu vào theo report code",
            description = """
        Trả về danh sách tham số đầu vào, hỗ trợ tìm kiếm theo từ khóa và phân trang.
        - reportCode: Mã báo cáo
        - keyword: Từ khóa tìm kiếm theo tên/miêu tả
        - pageable: thông tin phân trang (page, size, sort)
        """
    )
    @LogActivity(function = "Lấy danh sách tham số đầu vào")
    @GetMapping("/search-all")
    public ResponseEntity<ResponseData<PageResponse<ReportParamDto>>> searchAll(@RequestParam String reportCode,
                                                                                @RequestParam(required = false) String keyword,
                                                                                Pageable pageable) {
        Page<ReportParamDto> page = ctgCfgReportParamService.searchAllByReportCode(reportCode, keyword, pageable);
        PageResponse<ReportParamDto> response = new PageResponse<>(page);
        return ResponseData.okEntity(response);
    }

    @LogActivity(function = "Tạo tham số đầu vào")
    @Operation(summary = "Tạo mới tham số đầu vào", description = "Tạo mới một tham số đầu vào" +
            "- ParameterCode phải là duy nhất")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<CtgCfgReportParamDTO>> create(@RequestBody CtgCfgReportParamDTO ctgCfgReportParamDTO) {
        CtgCfgReportParamDTO newCfgReportParamDTO = ctgCfgReportParamService.create(ctgCfgReportParamDTO);
        return ResponseData.okEntity(newCfgReportParamDTO);
    }

    @LogActivity(function = "Cập nhật tham số đầu vào")
    @Operation(summary = "Cập nhật tham số đầu vào", description = "Cập nhật một tham số đầu vào")
    @PutMapping("/update/{parameterCode}")
    public ResponseEntity<ResponseData<CtgCfgReportParamDTO>> update(@PathVariable String parameterCode,
                                                                     @RequestBody CtgCfgReportParamDTO ctgCfgReportParamDTO) {
        CtgCfgReportParamDTO newCfgReportParamDTO =  ctgCfgReportParamService.update(ctgCfgReportParamDTO);
        return ResponseData.okEntity(newCfgReportParamDTO);
    }

    @LogActivity(function = "Xóa tham số đầu vào")
    @Operation(summary = "Xóa tham số đầu vào", description = "Xóa một tham số đầu vào")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable Long id) {
        ctgCfgReportParamService.deleteById(id);
        return ResponseData.noContentEntity();
    }

    @LogActivity(function = "Lấy danh sách giá trị tham số nguồn")
    @Operation(summary = "Lấy danh sách gía trị tham số nguồn thông qua exec resource sql", description = "Lấy danh sách gía trị tham số nguồn")
    @GetMapping("/get-source")
    public ResponseEntity<ResponseData<List<ResourceParamDTO>>> getResourceParams(@RequestParam String parameterCode,
                                                                                  @RequestParam String reportCode) {
        List<ResourceParamDTO> resourceParamDTOS = ctgCfgReportParamService.execResourceSql(parameterCode, reportCode);
        return ResponseData.okEntity(resourceParamDTOS);
    }

    @LogActivity(function = "Lấy thông tin tham số báo cáo")
    @Operation(summary = "Lấy thông tin tham số báo cáo", description = "Lấy Thông tin tham báo cáo")
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseData<CtgCfgReportParamDTO>> getReportParam(@PathVariable Long id) {
        CtgCfgReportParamDTO resourceParamDTO = ctgCfgReportParamService.findById(id);
        return ResponseData.okEntity(resourceParamDTO);
    }

    @LogActivity(function = "Lấy combobox tên tham số nguồn")
    @Operation(summary = "Combobox tên tham số nguồn", description = "Combobox tên tham số nguồn")
    @GetMapping("/get-all-resource-name")
    public ResponseEntity<ResponseData<List<ReportParamDto>>> getAllResourceParamName(@RequestParam String reportCode) {
        List<ReportParamDto> reportParamDtos = ctgCfgReportParamService.getAllResourceParamName(reportCode);
        return ResponseData.okEntity(reportParamDtos);
    }

    @LogActivity(function = "Lấy combobox tên tham số đích")
    @Operation(summary = "Combobox tên tham số đích", description = "Combobox tên tham số đích")
    @GetMapping("/get-all-target-name")
    public ResponseEntity<ResponseData<List<ReportParamDto>>> getAllTargetParamNames(@RequestParam String reportCode) {
        List<ReportParamDto> reportParamDtos = ctgCfgReportParamService.getAllTargetParamNames(reportCode);
        return ResponseData.okEntity(reportParamDtos);
    }

    @LogActivity(function = "Lấy tham số mining theo report")
    @Operation(summary = "Lấy thông tin tham số báo cáo", description = "Lấy Thông tin tham báo cáo")
    @GetMapping("/get-mining-param-by-report/{reportCode}")
    public ResponseEntity<ResponseData<List<ReportMiningParamDTO>>> getAllByReport(@PathVariable String reportCode) {
        List<ReportMiningParamDTO> reportMiningParamDTOS = ctgCfgReportParamService.getAllByReport(reportCode);
        return ResponseData.okEntity(reportMiningParamDTOS);
    }

    @LogActivity(function = "Kiểm tra tham số tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String reportCode,@RequestParam String parameterCode) {
        return ResponseData.okEntity(ctgCfgReportParamService.checkExist(reportCode,parameterCode));
    }

}
