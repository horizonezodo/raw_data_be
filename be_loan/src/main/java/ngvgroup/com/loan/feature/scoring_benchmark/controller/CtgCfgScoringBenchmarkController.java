package ngvgroup.com.loan.feature.scoring_benchmark.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.scoring_benchmark.dto.CtgCfgScoringBenchmarkDTO;
import ngvgroup.com.loan.feature.scoring_benchmark.dto.ListCtgCfgScoringBenchmark;
import ngvgroup.com.loan.feature.scoring_benchmark.service.CtgCfgScoringBenchmarkService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ctg-cfg-scoring-benchmark")
@PreAuthorize("hasRole('profile_scoring_benchmark')")
public class CtgCfgScoringBenchmarkController {
    private final CtgCfgScoringBenchmarkService ctgCfgScoringBenchmarkService;

    @Operation(
            summary = "Danh sách điểm chuẩn xếp hạng"
    )
    @GetMapping
    public ResponseEntity<ResponseData<Page<ListCtgCfgScoringBenchmark>>> getAllScoringBenchmark(@RequestParam("keyword") String keyword, @ParameterObject Pageable pageable){
        Page<ListCtgCfgScoringBenchmark> page = ctgCfgScoringBenchmarkService.pageScoringBenchmark(keyword,pageable);
        return ResponseData.okEntity(page);
    }

    @Operation(
            summary = "Chi tiết điểm chuẩn xếp hạng"
    )
    @GetMapping("/{benchmarkCode}")
    public ResponseEntity<ResponseData<CtgCfgScoringBenchmarkDTO>> getOne(@PathVariable("benchmarkCode")String benchmarkCode){
        return ResponseData.okEntity(ctgCfgScoringBenchmarkService.getOne(benchmarkCode));
    }

    @Operation(
            summary = "Khởi tạo điểm chuẩn xếp hạng"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createScoringBenchmark(@RequestBody CtgCfgScoringBenchmarkDTO dto){
        ctgCfgScoringBenchmarkService.createScoringBenchmark(dto);
        return ResponseData.createdEntity();
    }

    @Operation(
            summary = "Sửa điểm chuẩn xếp hạng"
    )
    @PostMapping("/update/{benchmarkCode}")
    public ResponseEntity<ResponseData<Void>> updateScoringBenchmark(@RequestBody CtgCfgScoringBenchmarkDTO dto, @PathVariable("benchmarkCode")String benchmarkCode){
        ctgCfgScoringBenchmarkService.updateScoringBenchmark(dto,benchmarkCode);
        return ResponseData.noContentEntity();
    }

    @Operation(
            summary = "Xóa điểm chuẩn xếp hạng"
    )
    @PostMapping("/delete/{benchmarkCode}")
    public ResponseEntity<ResponseData<Void>> deleteScoringBenchmark(@PathVariable("benchmarkCode")String benchmarkCode){
        ctgCfgScoringBenchmarkService.deleteScoringBenchmark(benchmarkCode);
        return ResponseData.noContentEntity();
    }

    @Operation(
            summary = "Xuất danh sách điểm chuẩn xếp hạng"
    )
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportExcel(@PathVariable("fileName")String fileName, @RequestBody CtgCfgScoringBenchmarkDTO dto){
        return ctgCfgScoringBenchmarkService.exportToExcel(dto,null,fileName);
    }

    @GetMapping("/check-benchmarkcode")
    public ResponseEntity<Boolean> checkBenchmarkCode(
            @RequestParam("value") String value) {
        boolean exist = ctgCfgScoringBenchmarkService.existByBenchmarkCode(value);
        return ResponseEntity.ok(exist);
    }
}
