package ngvgroup.com.rpt.features.dim.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.dim.dto.DimCiDDTO;
import ngvgroup.com.rpt.features.dim.service.DimCiDService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("dim-cid")
public class DimCiDController {
    private final DimCiDService service;

    @LogActivity(function = "Lấy tất cả CI dữ liệu")
    @GetMapping
    public ResponseEntity<ResponseData<List<DimCiDDTO>>>getAll(){
        return ResponseData.okEntity(service.listDim());
    }
}
