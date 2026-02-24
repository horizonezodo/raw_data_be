package ngvgroup.com.hrm.feature.category.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgPositionDTO;
import ngvgroup.com.hrm.feature.category.model.HrmCfgPosition;
import ngvgroup.com.hrm.feature.category.service.HrmCfgPositionService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/position")
@PreAuthorize("hasRole('category_common_position')")
public class HrmCfgPositionController extends BaseController<HrmCfgPosition, HrmCfgPositionDTO, HrmCfgPositionService> {

    public HrmCfgPositionController(HrmCfgPositionService service) {
        super(service);
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<HrmCfgPositionDTO>>> search(@RequestParam String keyword, @ParameterObject Pageable pageable){
       Page<HrmCfgPositionDTO> page = service.search(keyword, pageable);
        return ResponseData.okEntity(page);
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportData(@RequestParam String fileName) throws BusinessException {
        return service.exportExcel(fileName);
    }
}
