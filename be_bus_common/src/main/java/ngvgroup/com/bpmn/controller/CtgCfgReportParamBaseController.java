package ngvgroup.com.bpmn.controller;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.bpmn.dto.request.ExportExcelRequest;
import ngvgroup.com.bpmn.dto.request.SearchFilterRequest;
import ngvgroup.com.bpmn.dto.response.ResponseData;
import ngvgroup.com.bpmn.service.CtgCfgReportParamBaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com-cfg-report-param-base")
public class CtgCfgReportParamBaseController {
    private final CtgCfgReportParamBaseService ctgCfgReportParamBaseService;

    public CtgCfgReportParamBaseController(CtgCfgReportParamBaseService ctgCfgReportParamBaseService) {
        this.ctgCfgReportParamBaseService = ctgCfgReportParamBaseService;
    }

    @PostMapping
    public ResponseEntity<ResponseData<Void>> createComCfgReportParamBase(
            @RequestBody CtgCfgReportParamBaseDto comCfgReportParamBase) {
        ctgCfgReportParamBaseService.createCfgReportParamBase(comCfgReportParamBase);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseData<Void>> updateComCfgReportParamBase(
            @PathVariable("id") long id,
            @RequestBody CtgCfgReportParamBaseDto comCfgReportParamBase
    ) {
        ctgCfgReportParamBaseService.updateCfgReportParamBase(id, comCfgReportParamBase);
        return ResponseData.noContentEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CtgCfgReportParamBaseDto>> getDetailComCfgReportParamBase(@PathVariable("id") long id) {
        return ResponseData.okEntity(ctgCfgReportParamBaseService.getDetailCfgReportParamBase(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<Void>> deleteComCfgReportParamBase(@PathVariable("id") long id) {
        ctgCfgReportParamBaseService.deleteCfgReportParamBase(id);
        return ResponseData.noContentEntity();
    }

//    @PostMapping("/search")
//    public ResponseEntity<ResponseData<PageResponse<CtgCfgReportParamBaseResponse>>> searchComCfgReportParamBase(
//            @RequestBody SearchFilterRequest request){
//
//        return ResponseData.okEntity(ctgCfgReportParamBaseService.searchCfgReportParamBase(request));
//    }
        @PostMapping("/search")
        public ResponseEntity<ResponseData<List<CtgCfgReportParamBaseResponse>>> searchComCfgReportParamBase(
                @RequestBody SearchFilterRequest request){

            return ResponseData.okEntity(ctgCfgReportParamBaseService.searchCfgReportParamBasev2(request));
        }

    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportComCfgMailTemplate(
            @PathVariable("fileName") String fileName,
            @RequestBody ExportExcelRequest request) {
        return ctgCfgReportParamBaseService.exportExcel(fileName, request);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgCfgReportParamBaseResponse>>> getAllComCfgReportParamBase() {
        return ResponseData.okEntity(ctgCfgReportParamBaseService.getAllCfgReportParamBase());
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExistComCfgReportParamBase(@RequestParam String paramBaseCode) {
        return ResponseData.okEntity(ctgCfgReportParamBaseService.isExistedByParamBaseCode(paramBaseCode));
    }
    
}
