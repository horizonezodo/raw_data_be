package ngvgroup.com.bpm.features.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.common.dto.CtgComCommonDTO;
import ngvgroup.com.bpm.features.common.service.CtgComCommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class CtgComCommonController {
    private final CtgComCommonService ctgComCommonService;

    @Operation(
            summary = "Lấy danh sách common đầy đủ theo commonTypeCode"
    )
    @GetMapping("/type-code")
    public ResponseEntity<ResponseData<List<CtgComCommonDTO>>> findAllByCommonTypeCode(@RequestParam("commonTypeCode") String commonTypeCode) {
        List<CtgComCommonDTO> result = ctgComCommonService.findAllByCommonTypeCode(commonTypeCode);
        return ResponseData.okEntity(result);
    }

}
