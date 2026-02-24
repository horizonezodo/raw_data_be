package ngvgroup.com.bpmn.controller;

import com.ngvgroup.bpm.core.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpmn.dto.ComCommon.CommonDto;
import ngvgroup.com.bpmn.dto.CtgComCommon.CtgComCommonDTO;
import ngvgroup.com.bpmn.service.CtgComCommonService;
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
    public ResponseEntity<ResponseData<List<CommonDto>>> getListCommon(@RequestParam("commonTypeCode") String commonTypeCode) {
        List<CommonDto> listCommon = ctgComCommonService.listCommon(commonTypeCode);
        return ResponseData.okEntity(listCommon);
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
