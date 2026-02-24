package ngvgroup.com.loan.feature.scoring_type.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO;
import ngvgroup.com.loan.feature.scoring_type.service.CtgCfgScoringTypeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ctg-cfg-scoring-type")
@RequiredArgsConstructor
public class CtgCfgScoringTypeController {
    private final CtgCfgScoringTypeService ctgCfgScoringTypeService;

    @Operation(
            summary = "Danh sách loại xếp hạng tín dụng"
    )
    @GetMapping
    public ResponseEntity<ResponseData<List<CtgCfgScoringTypeDTO>>> getAllData(){
        return ResponseData.okEntity(ctgCfgScoringTypeService.getAllData());
    }

    @Operation(
            summary = "Danh sách phân trang loại xếp hạng tín dụng"
    )
    @GetMapping("/all")
    public ResponseEntity<ResponseData<Page<CtgCfgScoringTypeDTO>>> pageData(@RequestParam("keyword") String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgCfgScoringTypeService.pageData(keyword, pageable));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgCfgScoringTypeDTO>>> getAllCtgCfgScoringTypes(){
        return ResponseData.okEntity(ctgCfgScoringTypeService.getAll());
    }

    @GetMapping("/search-all")
    public ResponseEntity<ResponseData<Page<CtgCfgScoringTypeDTO>>> searchAll(@RequestParam("keyword") String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgCfgScoringTypeService.searchAll(keyword,pageable));
    }

    @PostMapping("/export-to-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam("keyword") String keyword, @PathVariable("fileName") String fileName, @RequestBody List<String> label){
        return ctgCfgScoringTypeService.exportToExcel(keyword,fileName,label);
    }

    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgScoringTypeDTO scoringTypeDTO){
        ctgCfgScoringTypeService.create(scoringTypeDTO);
        return ResponseData.createdEntity();
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgScoringTypeDTO scoringTypeDTO){
        ctgCfgScoringTypeService.update(scoringTypeDTO);
        return ResponseData.createdEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam("scoringTypeCode") String scoringTypeCode){
        ctgCfgScoringTypeService.delete(scoringTypeCode);
        return ResponseData.noContentEntity();
    }


    @PostMapping("/upload-file")
    public ResponseEntity<ResponseData<Void>> uploadFile(@RequestParam(value = "file",required = false) MultipartFile file,
                                                         @RequestParam("objectCode") String objectCode) {
        ctgCfgScoringTypeService.uploadFile(file,objectCode);
        return ResponseData.createdEntity();
    }

    @PostMapping("/download-file")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("objectCode") String objectCode) {
        return ctgCfgScoringTypeService.downloadFile(objectCode);
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<ResponseData<Void>> deleteFile(@RequestParam("objectCode") String objectCode) {
        ctgCfgScoringTypeService.deleteFile(objectCode);
        return ResponseData.noContentEntity();
    }

    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<CtgCfgScoringTypeDTO>> getDetail(@RequestParam("scoringTypeCode") String scoringTypeCode){
        return ResponseData.okEntity(ctgCfgScoringTypeService.getDetail(scoringTypeCode));
    }


    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam("scoringTypeCode") String scoringTypeCode) {
        return ResponseData.okEntity(ctgCfgScoringTypeService.checkExist(scoringTypeCode));
    }
}
