package ngvgroup.com.fac.feature.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO;
import ngvgroup.com.fac.feature.common.service.CtgCfgCommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CtgCtgCommonController {
    private final CtgCfgCommonService service;

    @GetMapping
    public ResponseEntity<ResponseData<List<CtgCfgCommonDTO>>> getAll(
            @RequestParam(required = false) String commonTypeCode,
            @RequestParam(required = false) String parentCode) {
        return ResponseData.okEntity(this.service.getByTypeCodeAndParentCode(commonTypeCode, parentCode));
    }

    @GetMapping("get-field")
    public ResponseEntity<ResponseData<CtgCfgCommonDTO>> getCommonNameByCommonValue(@RequestParam String commonValue) {
        return ResponseData.okEntity(this.service.getCommonNameBuCommonValue(commonValue));
    }

    @GetMapping("get-list")
    public ResponseEntity<ResponseData<List<CtgCfgCommonDTO>>> getList(@RequestParam List<String> commonCode) {
        return ResponseData.okEntity(this.service.getList(commonCode));
    }
}
