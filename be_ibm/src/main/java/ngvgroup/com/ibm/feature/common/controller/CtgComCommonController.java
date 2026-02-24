package ngvgroup.com.ibm.feature.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.ibm.feature.common.dto.CtgComCommonDTO;
import ngvgroup.com.ibm.feature.common.service.CtgComCommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/common")
public class CtgComCommonController {
    private final CtgComCommonService ctgComCommonService;

    @Operation(summary = "Lấy danh sách common theo common type code", description = "Lấy danh sách common theo common type code", responses = {
    })
    @GetMapping("/type-code/{commonTypeCode}")
    public ResponseEntity<ResponseData<List<CtgComCommonDTO>>> getAllByCommonTypeCode(@PathVariable String commonTypeCode) {
        List<CtgComCommonDTO> ctgComCommonDTOS = ctgComCommonService.findAllByCommonTypeCode(commonTypeCode);
        return ResponseData.okEntity(ctgComCommonDTOS);
    }
}
