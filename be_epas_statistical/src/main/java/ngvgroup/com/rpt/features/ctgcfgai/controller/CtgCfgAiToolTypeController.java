package ngvgroup.com.rpt.features.ctgcfgai.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.CtgCfgAiToolTypeDTO;
import ngvgroup.com.rpt.features.ctgcfgai.service.CtgCfgAiToolTypeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai-type")
@RequiredArgsConstructor
public class CtgCfgAiToolTypeController {
    private final CtgCfgAiToolTypeService service;
    @LogActivity(function = "Lấy danh sách loại công cụ AI")
    @Operation(summary = "Lấy danh sách loại công cụ AI")
    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgAiToolTypeDTO>>> pageAiType(@RequestParam String keyword,
                                                                                @ParameterObject Pageable pageable){
        return ResponseData.okEntity(service.page(keyword, pageable));
    }
    @LogActivity(function = "Lấy thông tin chi tiết loại công cụ AI")
    @Operation(summary = "Lấy thông tin chi tiết loại công cụ AI")
    @GetMapping("/get/{code}")
    public ResponseEntity<ResponseData<CtgCfgAiToolTypeDTO>> getDetail(@PathVariable("code")String code){
        return ResponseData.okEntity(service.getDetail(code));
    }
    @LogActivity(function = "Tạo mới loại công cụ AI")
    @Operation(summary = "Tạo mới loại công cụ AI")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createAIType(@RequestBody CtgCfgAiToolTypeDTO dto){
        service.createAiTool(dto);
        return ResponseData.createdEntity();
    }
    @LogActivity(function = "Sửa loại công cụ AI")
    @Operation(summary = "Sửa công cụ AI")
    @PostMapping("/update/{code}")
    public ResponseEntity<ResponseData<Void>> updateAiType(@RequestBody CtgCfgAiToolTypeDTO dto, @PathVariable("code")String code){
        service.updateAiTool(dto, code);
        return ResponseData.okEntity();
    }
    @LogActivity(function = "Xóa loại công cụ AI")
    @Operation(summary = "Xóa công cụ AI")
    @PostMapping("/delete/{code}")
    public  ResponseEntity<ResponseData<Void>> deleteAiType(@PathVariable("code")String code){
        service.deleteAiTool(code);
        return ResponseData.okEntity();
    }
    @LogActivity(function = "Xuất danh sách loại công cụ AI")
    @Operation(summary = "Xuất file danh sách loại công cụ AI")
    @PostMapping("/export-to-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam List<String> labels, @RequestParam String keyword, @RequestParam String fileName){
        return service.exportToExcel(labels,keyword,fileName);
    }

    @LogActivity(function = "Lấy danh sách tất cả loại công cụ AI")
    @GetMapping("/list")
    public ResponseEntity<ResponseData<List<CtgCfgAiToolTypeDTO>>> listAiType(){
        return ResponseData.okEntity(service.listAiTool());
    }
}
