package ngvgroup.com.rpt.features.ctgcfgstatscorerank.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupRequest;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupSearch;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupResponse;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreGroup;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.CtgCfgStatScoreGroupService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stat-score-group")
@RequiredArgsConstructor
public class CtgCfgStatScoreGroupController {
    private final CtgCfgStatScoreGroupService service;

    @LogActivity(function = "Tìm kiếm tất cả nhóm điểm")
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<Page<CtgCfgStatScoreGroupDto>>> searchAll(@RequestParam String keyword, @RequestParam String statScoreTypeCode, @ParameterObject Pageable pageable) {

        return ResponseData.okEntity(service.searchAll(keyword, statScoreTypeCode, pageable));
    }

    @LogActivity(function = "Lấy danh sách nhóm điểm")
    @PostMapping("get-list-score-group")
    public ResponseEntity<ResponseData<Page<CtgCfgStatScoreGroupResponse>>> getAll(
            @RequestBody StatScoreGroupSearch request, Pageable pageable) {
        return ResponseData.okEntity(service.getAll(request, pageable));
    }

    @LogActivity(function = "Thêm nhóm điểm")
    @PostMapping("add")
    public ResponseEntity<ResponseData<CtgCfgStatScoreGroup>> add(@RequestBody StatScoreGroupRequest req) {
        return ResponseData.okEntity(service.add(req));
    }

    @LogActivity(function = "Lấy chi tiết nhóm điểm")
    @GetMapping("detail")
    public ResponseEntity<ResponseData<CtgCfgStatScoreGroupResponse>> detail(@RequestParam String statScoreGroupCode) {
        return ResponseData.okEntity(service.getDetail(statScoreGroupCode));
    }

    @LogActivity(function = "Cập nhật nhóm điểm")
    @PutMapping("update")
    public ResponseEntity<ResponseData<CtgCfgStatScoreGroup>> edit(@RequestBody StatScoreGroupRequest req) {
        return ResponseData.okEntity(service.edit(req));
    }

    @LogActivity(function = "Xóa nhóm điểm")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseData<String>> delete(@RequestParam String statScoreGroupCode) {
        service.delete(statScoreGroupCode);
        return ResponseData.okEntity("Delete Successfully");
    }

    @LogActivity(function = "Xuất Excel nhóm điểm")
    @PostMapping("/download-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam List<String> labels,
            @RequestParam(required = false) List<String> statScoreGroupCode,
            @RequestParam String fileName) {
        return service.exportToExcel(labels, statScoreGroupCode, fileName);
    }

    @LogActivity(function = "Kiểm tra nhóm điểm tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String statScoreGroupCode) {
        return ResponseData.okEntity(service.checkExistStatScoreGroupCode(statScoreGroupCode));
    }
}
