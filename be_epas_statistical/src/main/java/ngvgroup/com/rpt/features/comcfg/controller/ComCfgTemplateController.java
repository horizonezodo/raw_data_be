package ngvgroup.com.rpt.features.comcfg.controller;

import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.features.comcfg.service.ComCfgTemplateService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/com-cfg-template")
public class ComCfgTemplateController {

    private ComCfgTemplateService comCfgTemplateService;

    @LogActivity(function = "Tìm kiếm tất cả mẫu cấu hình")
    @PostMapping("/search-all")
    public ResponseEntity<Object> searchAll(
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    ) {
        return comCfgTemplateService.getAll(keyword, pageable);
    }
}
