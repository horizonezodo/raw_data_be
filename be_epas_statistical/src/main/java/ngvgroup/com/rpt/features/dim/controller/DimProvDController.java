package ngvgroup.com.rpt.features.dim.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.features.dim.model.DimProvD;
import ngvgroup.com.rpt.features.dim.service.DimProvDService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/dim-prov-d")
public class DimProvDController {

    private final DimProvDService service;

    @LogActivity(function = "Lấy tất cả tỉnh thành theo CI")
    @GetMapping("/get-all/{ciId}")
    public ResponseEntity<ResponseData<List<DimProvD>>> getAll(@PathVariable String ciId) {
        return ResponseData.okEntity(service.getAll(ciId));
    }
}
