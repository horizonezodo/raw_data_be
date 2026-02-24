package ngvgroup.com.rpt.features.ctgcfgstattemplate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatRegulatoryInfo;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryRequest;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatorySearch;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatory;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatRegulatoryService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ctg-cfg-stat-regulatory")
@RequiredArgsConstructor
public class CtgCfgStatRegulatoryController {
    private final CtgCfgStatRegulatoryService service;

    @LogActivity(function = "Thêm quy định thống kê")
    @PostMapping("add")
    public ResponseEntity<ResponseData<CtgCfgStatRegulatory>> add(@RequestBody CtgCfgStatRegulatoryRequest request) {
        return ResponseData.okEntity(service.addCtgCfgStatRegulatory(request));
    }

    @LogActivity(function = "Cập nhật quy định thống kê")
    @PutMapping("/update")
    public ResponseEntity<ResponseData<CtgCfgStatRegulatory>> updateCtgCfgStatRegulatory(
            @RequestBody CtgCfgStatRegulatoryRequest request) {
        return ResponseData.okEntity(service.updateCtgCfgStatRegulatory(request));
    }

    @LogActivity(function = "Lấy tất cả quy định thống kê")
    @PostMapping("get-all")
    public ResponseEntity<ResponseData<Page<CtgCfgStatRegulatoryDto>>> getAll(
            @RequestBody CtgCfgStatRegulatorySearch search, Pageable pageable
    ) {
        return ResponseData.okEntity(service.getAllCtgCfgStatRegulatory(search, pageable));
    }

    @LogActivity(function = "Lấy chi tiết quy định thống kê")
    @GetMapping("/{statRegulatoryCode}")
    public ResponseEntity<ResponseData<CtgCfgStatRegulatoryResponse>> getDetail(@PathVariable String statRegulatoryCode) {
        CtgCfgStatRegulatoryResponse dto = service.getDetail(statRegulatoryCode);
        return ResponseData.okEntity(dto);
    }

    @LogActivity(function = "Xóa quy định thống kê")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<String>> deleteCtgCfgStatRegulatory(
            @PathVariable Long id) throws BusinessException {
        service.deleteCtgCfgStatRegulatory(id);
        return ResponseData.okEntity("Delete Successfully!");
    }

    @LogActivity(function = "Lấy quy định thống kê theo mã loại")
    @GetMapping("stat-regulatory-by-stat-type-code")
    public ResponseEntity<ResponseData<List<StatRegulatoryInfo>>> getStatRegulatoryByStatTypeCode(@RequestParam String statTypeCode, @RequestParam(defaultValue = "") String keyword) {
        return ResponseData.okEntity(service.getStatRegulatoryByStatTypeCode(statTypeCode, keyword));
    }

    @LogActivity(function = "Xuất Excel quy định thống kê")
    @PostMapping("/download-excel")
    @Operation(summary = "Xuất Excel loại thống kê")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam List<String> labels,
            @RequestParam(required = false) List<String> statRegulatoryCode,
            @RequestParam String fileName) {
        return service.exportToExcel(labels, statRegulatoryCode, fileName);
    }

}
