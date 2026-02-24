package ngvgroup.com.bpmn.controller;

import ngvgroup.com.bpmn.dto.CtgCfgReportGroup.CtgCfgReportGroupDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportGroupDTO.CtgCfgReportGroupDTO;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;
import ngvgroup.com.bpmn.dto.request.SearchFilterRequest;
import ngvgroup.com.bpmn.dto.response.ResponseData;
import ngvgroup.com.bpmn.service.CtgCfgReportGroupService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report-group")
public class CtgCfgReportGroupController {

    private CtgCfgReportGroupService ctgCfgReportGroupService;

    public CtgCfgReportGroupController(CtgCfgReportGroupService ctgCfgReportGroupService) {
        this.ctgCfgReportGroupService = ctgCfgReportGroupService;
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseData<Page<CtgCfgReportGroupDto>>> getListReportGroup(@ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgCfgReportGroupService.getListReportGroups(pageable));
    }

    @PostMapping("/find-list")
    public ResponseEntity<ResponseData<Page<CtgCfgReportGroupDto>>>findListReportGroup(@RequestBody SearchFilterRequest searchFilterRequest) {
        return ResponseData.okEntity(ctgCfgReportGroupService.findListReportGroups(searchFilterRequest));
    }

    @PostMapping("/export-to-excel/{fileName}")
    public ResponseEntity<byte[]>exportToExcel(@RequestBody CtgCfgReportGroupDto ctgCfgReportGroupDto, @PathVariable("fileName") String fileName) {
        return ctgCfgReportGroupService.exportToExcel(ctgCfgReportGroupDto, fileName);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createReportGroup(@RequestBody CtgCfgReportGroupDto ctgCfgReportGroupDto)  {
        ctgCfgReportGroupService.createReportGroup(ctgCfgReportGroupDto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseData<Void>> updateReportGroup(@RequestBody CtgCfgReportGroupDto ctgCfgReportGroupDto)  {
        ctgCfgReportGroupService.updateReportGroup(ctgCfgReportGroupDto);
        return ResponseData.createdEntity();
    }

    @GetMapping("/get-info/{reportGroupCode}")
    public ResponseEntity<ResponseData<CtgCfgReportGroupDto>> getInfoReportGroup(@PathVariable("reportGroupCode") String reportGroupCode) {
        return ResponseData.okEntity(ctgCfgReportGroupService.getInfoReportGroup(reportGroupCode));
    }

    @DeleteMapping("/delete/{reportGroupCode}")
    public ResponseEntity<ResponseData<Void>> deleteReportGroup(@PathVariable("reportGroupCode") String reportGroupCode)  {
        ctgCfgReportGroupService.deleteReportGroup(reportGroupCode);
        return ResponseData.createdEntity();
    }

    @Operation(summary = "Lấy danh sách report group", description = "Trả về danh sách report group")
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgCfgReportGroupDTO>>> getAll() {
        List<CtgCfgReportGroupDTO> ctgCfgReportGroupDTOS = ctgCfgReportGroupService.getAll();
        return ResponseData.okEntity(ctgCfgReportGroupDTOS);
    }

    @GetMapping("/get-list")
    public ResponseEntity<ResponseData<List<CtgCfgReportGroupDto>>>getList(){
        return ResponseData.okEntity(ctgCfgReportGroupService.getListReportGroups());
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String reportGroupCode) {
        return ResponseData.okEntity(ctgCfgReportGroupService.checkExist(reportGroupCode));
    }
}
