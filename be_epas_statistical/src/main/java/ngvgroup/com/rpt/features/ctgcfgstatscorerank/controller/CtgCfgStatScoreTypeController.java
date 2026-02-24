package ngvgroup.com.rpt.features.ctgcfgstatscorerank.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreType;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.CtgCfgStatScoreTypeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stat-score-type")
@RequiredArgsConstructor
public class CtgCfgStatScoreTypeController {
    private final CtgCfgStatScoreTypeService ctgCfgStatScoreTypeService;

    @LogActivity(function = "Lấy tất cả loại điểm")
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<Page<CtgCfgStatScoreTypeDto>>> getAll(@RequestParam String keyword, @ParameterObject Pageable pageable) {

        return ResponseData.okEntity(ctgCfgStatScoreTypeService.getAll(keyword,pageable));
    }

    @LogActivity(function = "Liệt kê tất cả loại điểm")
    @GetMapping("list-all")
    public ResponseEntity<ResponseData<List<CtgCfgStatScoreType>>> listAll() {
        return ResponseData.okEntity(ctgCfgStatScoreTypeService.listAll());
    }

    @LogActivity(function = "Xuất Excel loại điểm")
    @PostMapping("/export-to-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam List<String> labels, @RequestParam String keyword, @RequestParam String fileName){
        return ctgCfgStatScoreTypeService.exportToExcel(labels,keyword,fileName);
    }

    @LogActivity(function = "Tạo mới loại điểm")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgStatScoreTypeDto ctgCfgStatScoreTypeDto) {
        ctgCfgStatScoreTypeService.create(ctgCfgStatScoreTypeDto);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Cập nhật loại điểm")
    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgStatScoreTypeDto ctgCfgStatScoreTypeDto) {
        ctgCfgStatScoreTypeService.update(ctgCfgStatScoreTypeDto);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Lấy chi tiết loại điểm")
    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<CtgCfgStatScoreTypeDto>> getDetail(@RequestParam Long id) {

        return ResponseData.okEntity(ctgCfgStatScoreTypeService.getDetail(id));
    }

    @LogActivity(function = "Xóa loại điểm")
    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam Long id) {
        ctgCfgStatScoreTypeService.delete(id);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Kiểm tra loại điểm tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String statScoreTypeCode) {
        return ResponseData.okEntity(ctgCfgStatScoreTypeService.checkExist(statScoreTypeCode));
    }
}
