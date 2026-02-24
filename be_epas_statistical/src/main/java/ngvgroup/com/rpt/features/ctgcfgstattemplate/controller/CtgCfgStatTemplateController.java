package ngvgroup.com.rpt.features.ctgcfgstattemplate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.*;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ctg-cfg-stat-template")
public class CtgCfgStatTemplateController {
    private final CtgCfgStatTemplateService service;
    private final ExportExcel exportExcel;

    @LogActivity(function = "Lấy dữ liệu cây mẫu biểu")
    @Operation(summary = "Lấy dữ liệu cây mẫu biểu")
    @GetMapping()
    public ResponseEntity<ResponseData<List<TreeData>>> getTreeData(@RequestParam(required = false) Boolean type){
        if(type == null) {
            return ResponseData.okEntity(service.getTreeData());
        } else {
            return ResponseData.okEntity(service.getTreeDataV2());
        }
    }

    @LogActivity(function = "Tìm kiếm mẫu biểu")
    @Operation(summary = "Tìm kiếm mẫu biểu")
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<CtgCfgStatTemplateDtoV3>>> getPageData(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> templateGroupCodes,
            @RequestParam(required = false) List<String> circularCode,
            @ParameterObject Pageable pageable
    ) {
        return ResponseData.okEntity(service.pageTemplate(keyword, templateGroupCodes, circularCode, pageable));
    }

    @LogActivity(function = "Xuất danh sách mẫu biểu")
    @Operation(summary = "Xuất danh sách mẫu biểu")
    @PostMapping("/export-to-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam List<String> labels,
                                                         @RequestBody Map<String,List<String>> frequenceMap,
                                                         @RequestParam String fileName){
        return service.exportToExcel(labels,frequenceMap,fileName);
    }

    @LogActivity(function = "Xóa mẫu biểu")
    @Operation(summary = "Xóa mẫu biểu theo TEMPLATE_CODE")
    @PostMapping("/delete/{templateCode}")
    public ResponseEntity<ResponseData<Void>> deleteTemplate(@PathVariable("templateCode")String templateCode){
        this.service.deleteTemplate(templateCode);
        return ResponseData.noContentEntity();
    }

    @LogActivity(function = "Lấy chi tiết mẫu biểu")
    @Operation(summary = "Lấy thông tin chi tiết mãu biểu theo TEMPLATE_CODE")
    @GetMapping("/get")
    public ResponseEntity<ResponseData<CreateFormDTO>>getOne(@RequestParam String templateCode){
        return ResponseData.okEntity(service.getDetail(templateCode));
    }

    @LogActivity(function = "Khởi tạo mẫu biểu")
    @Operation(summary = "Khởi tạo mẫu biểu")
    @PostMapping(
            value = "/create",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ResponseData<Void>> create(
            @RequestPart("dto") CreateFormDTO dto,
            @RequestPart(value = "templateFile", required = false) MultipartFile templateFile,
            @RequestPart(value = "templateReportFile", required = false) MultipartFile templateReportFile,
            @RequestPart(value = "userGuideFile", required = false) MultipartFile userGuideFile
    ) {
        service.createTemplate(dto, templateFile, templateReportFile, userGuideFile);
        return ResponseData.createdEntity();
    }
    @LogActivity(function = "Sửa mẫu biểu")
    @Operation(summary = "Sửa mẫu biểu")
    @PostMapping(
            value = "/update/{templateCode}",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<ResponseData<Void>> update( @RequestPart("dto") CreateFormDTO dto,
                                                      @RequestPart(value = "templateFile", required = false) MultipartFile templateFile,
                                                      @RequestPart(value = "templateReportFile", required = false) MultipartFile templateReportFile,
                                                      @RequestPart(value = "userGuideFile", required = false) MultipartFile userGuideFile,
                                                     @PathVariable("templateCode")String templateCode){
        this.service.updateTemplate(dto,templateCode,templateFile,templateReportFile,userGuideFile);
        return ResponseData.okEntity();
    }
    @LogActivity(function = "Lấy danh sách TEMPLATE_GROUP_CODE")
    @Operation(summary = "Lấy danh sách TEMPLATE_GROUP_CODE")
    @GetMapping("/list-template-group-code")
    public ResponseEntity<ResponseData<Page<CtgCfgStatTemplateDtoV1>>> getAllTemplateGroupCode(@RequestParam(required = false) String keyword,
                                                                              @ParameterObject Pageable pageable){
        return ResponseData.okEntity(this.service.getPageTemplateGroupCode(keyword, pageable));
    }

    @LogActivity(function = "Lấy danh sách quy tắc báo cáo")
    @PostMapping("/list-report-rule")
    public ResponseEntity<ResponseData<Page<ReportRuleDto>>> getListReportRule(
            @RequestBody FilterReportRuleDto filter ,
            @RequestParam String search ,
            Pageable pageable
    ){
        return ResponseData.okEntity(this.service.getListReportRule(filter , search , pageable));
    }

    @LogActivity(function = "Xuất quy tắc báo cáo")
    @PostMapping("/export-report-rule")
    public ResponseEntity<byte[]> exportReportRule(
            @RequestBody FilterReportRuleDto filter ,
            @RequestParam String search ,
            @RequestParam(required = false) String exportType,
            Pageable pageable
    ) throws Exception {
        if("all".equalsIgnoreCase(exportType)) {
            pageable = Pageable.unpaged();
        }
        Page<ReportRuleDto> data = this.service.getListReportRule(filter , search , pageable);
        List<ReportRuleDto> dataList = data.getContent();
        return exportExcel.exportExcel(dataList, "Report_Rule.xlsx");
    }

    @LogActivity(function = "Lấy tất cả dữ liệu mẫu biểu")
    @GetMapping("/all-data")
    public ResponseEntity<ResponseData<List<CtgCfgStatTemplateDtoV1>>> getAllData(){
        return ResponseData.okEntity(this.service.findALlTemplate());
    }

}
