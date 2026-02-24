package ngvgroup.com.bpm.features.sla.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.bpm.features.sla.service.ComCfgSlaProcessService;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaDto;
import ngvgroup.com.bpm.features.common.dto.CommonDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sla-process")
@PreAuthorize("hasRole('admin_sla')")
public class ComCfgSlaProcessController {

    private final ComCfgSlaProcessService cfgSlaProcessService;


    public ComCfgSlaProcessController(ComCfgSlaProcessService cfgSlaProcessService) {
        this.cfgSlaProcessService = cfgSlaProcessService;

    }

    @PostMapping("/get")
    public ResponseEntity<ResponseData<Page<ComCfgSlaDto.ComCfgSlaView>>> getSlaProcess(@ParameterObject Pageable pageable) {
        return ResponseData.okEntity(cfgSlaProcessService.getListSla(pageable));
    }

    @PostMapping("/find")
    public ResponseEntity<ResponseData<Page<ComCfgSlaDto.ComCfgSlaView>>> findSlaProcess(@RequestParam String keyword, @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(cfgSlaProcessService.findSlaByKeyword(keyword, pageable));
    }

    @GetMapping("/export-to-excel")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam String keyword,
            @RequestParam String fileName
    ) {
        return cfgSlaProcessService.exportToExcel(keyword, fileName);
    }

    @GetMapping("/get-unit")
    public ResponseEntity<ResponseData<List<CommonDto>>> getUnit() {
        return ResponseData.okEntity(cfgSlaProcessService.getUnit());
    }

    @GetMapping("/get-priority-level")
    public ResponseEntity<ResponseData<List<CommonDto>>> getPriorityLevel() {
        return ResponseData.okEntity(cfgSlaProcessService.getPriorityLevel());
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createProcess(@RequestBody ComCfgSlaDto comCfgSlaDto) {
        cfgSlaProcessService.create(comCfgSlaDto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseData<Void>> updateProcess(
            @RequestBody ComCfgSlaDto comCfgSlaDto) {

        cfgSlaProcessService.updateProcess(comCfgSlaDto);
        return ResponseData.okEntity(null);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ResponseData<Void>> deleteProcessSla(@RequestBody ComCfgSlaDto comCfgSlaDto) {
        cfgSlaProcessService.deleteProcessSla(comCfgSlaDto);
        return ResponseData.okEntity(null);
    }

    @GetMapping("/get-info/{orgCode}/{processTypeCode}")
    public ResponseEntity<ResponseData<ComCfgSlaDto>> getInfoProcess(@PathVariable("orgCode") String orgCode, @PathVariable("processTypeCode") String processTypeCode) {

        return ResponseData.okEntity(cfgSlaProcessService.getInfoProcess(orgCode, processTypeCode));
    }

    @PutMapping("/update-sla-warning-percent/{processDefineCode}/{orgCode}")
    public ResponseEntity<ResponseData<Void>> updateSlaWarningPercent(@PathVariable("processDefineCode") String processDefineCode, @PathVariable("orgCode") String orgCode) {

        cfgSlaProcessService.updateSlaWarningPercent(processDefineCode, orgCode);
        return ResponseData.okEntity(null);
    }
}

