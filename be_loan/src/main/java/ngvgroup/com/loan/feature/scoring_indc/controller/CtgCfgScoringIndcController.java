package ngvgroup.com.loan.feature.scoring_indc.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcAdd;
import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto;
import ngvgroup.com.loan.feature.scoring_indc.service.CtgCfgScoringIndcService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ctg-cfg-scoring-indc")
public class CtgCfgScoringIndcController {
    private final CtgCfgScoringIndcService ctgCfgScoringIndcService;

    @Operation(
            summary = "Danh sách chỉ tiêu xếp hạng"
    )
    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgScoringIndcDto>>> getAllScoring(@RequestParam("keyword") String keyword, @ParameterObject Pageable pageable){
        Page<CtgCfgScoringIndcDto> page= ctgCfgScoringIndcService.getAllScoringIndc(keyword, pageable);
        return ResponseData.okEntity(page);
    }

    @Operation(
            summary = "Xuất file danh sách chỉ tiêu xếp hạng"
    )
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportExcel(@PathVariable("fileName")String fileName, @RequestBody CtgCfgScoringIndcDto dto){
        return ctgCfgScoringIndcService.exportToExcel(dto,null,fileName);
    }

    @Operation(
            summary = "Thêm chỉ tiêu xếp hạng"
    )
    @PostMapping("/save")
    public ResponseEntity<ResponseData<Void>> createScoring(@RequestBody CtgCfgScoringIndcAdd ctgCfgScoringIndcAdd){
        ctgCfgScoringIndcService.addScoringIndc(ctgCfgScoringIndcAdd);
        return ResponseData.createdEntity();
    }

    @Operation(
            summary = "Cập nhật chỉ tiêu xếp hạng"
    )
    @PostMapping("/update/{indicatorCode}")
    public  ResponseEntity<ResponseData<Void>> updateScoring(@PathVariable("indicatorCode")String indicatorCode, @RequestBody CtgCfgScoringIndcAdd ctgCfgScoringIndcAdd){
        ctgCfgScoringIndcService.updateSrocingIndc(indicatorCode,ctgCfgScoringIndcAdd);
        return ResponseData.noContentEntity();
    }

    @Operation(
            summary = "Xóa chỉ tiêu xếp hạng"
    )
    @PostMapping("/delete/{indicatorCode}")
    public ResponseEntity<ResponseData<Void>> deleteScoring(@PathVariable("indicatorCode")String indicatorCode){
        ctgCfgScoringIndcService.deleteScoringIndc(indicatorCode);
        return ResponseData.noContentEntity();
    }

    @Operation(
            summary = "Xem chi tiết chỉ tiêu xếp hạng"
    )
    @GetMapping("/{indicatorCode}")
    public ResponseEntity<ResponseData<CtgCfgScoringIndcAdd>> getOne(@PathVariable("indicatorCode")String indicatorCode){
        CtgCfgScoringIndcAdd ctgCfgScoringIndcAdd = ctgCfgScoringIndcService.getOne(indicatorCode);
        return ResponseData.okEntity(ctgCfgScoringIndcAdd);
    }

    @Operation(
            summary = "Xem chi tiết chỉ tiêu xếp hạng trong mapping chỉ tiêu"
    )
    @GetMapping("/page")
    public ResponseEntity<ResponseData<Page<CtgCfgScoringIndcDto>>> getPage(@RequestParam("groupCode")String groupCode, @RequestParam("keyword") String keyword,@ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgCfgScoringIndcService.getAllScoringIndcMapp(groupCode,keyword, pageable));
    }

    @Operation(
            summary = "Check code runtime"
    )
    @GetMapping("/check")
    public ResponseEntity<ResponseData<Boolean>> checkCode(@RequestParam("indicatorCode")String indicatorCode){
        return ResponseData.okEntity(ctgCfgScoringIndcService.checkCode(indicatorCode));
    }
}
