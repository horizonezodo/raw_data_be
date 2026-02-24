package ngvgroup.com.loan.feature.ward.controller;

import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.ward.dto.CtgComWardDto;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.loan.feature.ward.service.CtgComWardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ctg-com-ward")
@AllArgsConstructor
public class CtgComWardController {
    private final CtgComWardService ctgComWardService;

    @GetMapping
    public ResponseEntity<ResponseData<List<CtgComWardDto>>> getAllCtgComWards() {
        return ResponseData.okEntity(ctgComWardService.getAll());
    }
}
