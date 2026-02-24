package ngvgroup.com.rpt.features.ctgcfgstat.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.CtgCfgStatCodeMapDto;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.StatCodeMapDto;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.StatCodeMappingResponse;
import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatCodeMap;
import ngvgroup.com.rpt.features.ctgcfgstat.service.CtgCfgStatCodeMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ctg-cfg-stat-code-map")
@RequiredArgsConstructor
public class CtgCfgStatCodeMapController {
    private final CtgCfgStatCodeMapService service;

    @LogActivity(function = "Lấy dữ liệu mapping theo mã loại thống kê")
    @GetMapping("/get-data-mapping")
    public ResponseEntity<ResponseData<List<StatCodeMappingResponse>>> getDataMappingByStatTypeCode(@RequestParam String statTypeCode) {
        return ResponseData.okEntity(service.queryInternalStatByTypeCode(statTypeCode));
    }

    @LogActivity(function = "Lấy dữ liệu mapping")
    @GetMapping("data-mapping")
    public ResponseEntity<ResponseData<List<StatCodeMapDto>>> getDataMapping(@RequestParam String statTypeCode, @RequestParam List<String> internalCode) {
        return ResponseData.okEntity(service.getDataMapping(statTypeCode, internalCode));
    }

    @LogActivity(function = "Lưu mapping mã thống kê")
    @PostMapping("/save-data")
    public ResponseEntity<ResponseData<List<CtgCfgStatCodeMap>>> saveCodeMapping(@RequestBody CtgCfgStatCodeMapDto dto) {
        return ResponseData.okEntity(service.saveCodeMapping(dto));
    }

    @LogActivity(function = "Xóa mapping mã thống kê")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseData<String>> deleteStatCodeMapping(@RequestParam String statTypeCode, @RequestParam String statRegulatoryCode) {
        service.deleteStatCodeMapByStatTypeCode(statTypeCode, statRegulatoryCode);
        return ResponseData.okEntity("Delete success!");
    }
}
