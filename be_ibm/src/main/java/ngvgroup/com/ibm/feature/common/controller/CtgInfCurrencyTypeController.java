package ngvgroup.com.ibm.feature.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.ibm.feature.common.dto.CtgInfCurrencyTypeDto;
import ngvgroup.com.ibm.feature.common.service.CtgInfCurrencyTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currency-type")
@AllArgsConstructor
public class CtgInfCurrencyTypeController {

    private final CtgInfCurrencyTypeService ctgInfCurrencyTypeService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgInfCurrencyTypeDto>>> getAllCurrencyTypes() {
        return ResponseData.okEntity(ctgInfCurrencyTypeService.getAllCtgInfCurrencyTypes());
    }
}
