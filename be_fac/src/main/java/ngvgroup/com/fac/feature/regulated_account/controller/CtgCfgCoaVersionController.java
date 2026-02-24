package ngvgroup.com.fac.feature.regulated_account.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaVersionDTO;
import ngvgroup.com.fac.feature.regulated_account.service.CtgCfgCoaVersionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coa-version")
public class CtgCfgCoaVersionController {
    private final CtgCfgCoaVersionService service;

    @GetMapping
    public ResponseEntity<ResponseData<List<CtgCfgCoaVersionDTO>>> getAll(){
        return ResponseData.okEntity(this.service.getAllData());
    }

    @GetMapping("/with-common")
    public ResponseEntity<ResponseData<List<CtgCfgCoaVersionDTO>>> getAllWithCommon(@RequestParam String commonTypeCode){
        return ResponseData.okEntity(this.service.getAllWithCommon(commonTypeCode));
    }
}
