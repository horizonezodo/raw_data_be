package ngvgroup.com.hrm.feature.category.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgTitleDTO;
import ngvgroup.com.hrm.feature.category.model.HrmCfgTitle;
import ngvgroup.com.hrm.feature.category.service.HrmCfgTitleService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/title")
@PreAuthorize("hasRole('category_common_position_title')")
public class HrmCfgTitleController extends BaseController<HrmCfgTitle, HrmCfgTitleDTO, HrmCfgTitleService> {

    public HrmCfgTitleController(HrmCfgTitleService service) {
        super(service);
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<HrmCfgTitleDTO>>> search(@RequestParam String keyword, @ParameterObject Pageable pageable){
        Page<HrmCfgTitleDTO> page = service.search(keyword, pageable);
        return ResponseData.okEntity(page);
    }

    @GetMapping("/{titleCode}")
    public ResponseEntity<ResponseData<HrmCfgTitleDTO>> getDetail(@PathVariable String titleCode){
        return ResponseData.okEntity(service.getDetail(titleCode));
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportData(@RequestParam String fileName) throws BusinessException {
        return service.exportExcel(fileName);
    }

    @DeleteMapping({"/by-code/{titleCode}"})
    public ResponseEntity<ResponseData<String>> deleteByTitleCode(@PathVariable("titleCode") String titleCode) {
        this.service.deleteByTitleCode(titleCode);
        return ResponseData.okEntity("Deleted successfully");
    }
}
