package ngvgroup.com.loan.feature.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.common.dto.CommonDto;
import ngvgroup.com.loan.feature.common.dto.CtgComCommonDTO;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.loan.feature.common.service.CtgComCommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class CtgComCommonController {
    private final CtgComCommonService ctgComCommonService;

    @Operation(
            summary = "Common dùng chung"
    )
    @GetMapping
    public ResponseEntity<ResponseData<List<CommonDto>>> findAll(@RequestParam("commonTypeCode") String commonTypeCode) {
        List<CommonDto> listCommon = ctgComCommonService.listCommon(commonTypeCode);
        return ResponseData.okEntity(listCommon);
    }

    @Operation(
            summary = "Danh sách tất cả common"
    )
    @GetMapping("/list")
    public ResponseEntity<ResponseData<List<CommonDto>>> getAll(){
        return ResponseData.okEntity(ctgComCommonService.getAllCommon());
    }

    @Operation(
            summary = "Lấy danh sách common đầy đủ theo commonTypeCode"
    )
    @GetMapping("/type-code")
    public ResponseEntity<ResponseData<List<CtgComCommonDTO>>> findAllByCommonTypeCode(@RequestParam("commonTypeCode") String commonTypeCode) {
        List<CtgComCommonDTO> result = ctgComCommonService.findAllByCommonTypeCode(commonTypeCode);
        return ResponseData.okEntity(result);
    }
}
