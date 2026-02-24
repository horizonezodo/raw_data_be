package ngvgroup.com.rpt.features.ctgcfgstatruletype.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.StatRuleTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.ResponseSearchStatRuleTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.model.StatRuleType;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.service.StatRuleTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/stat-rule-type")
public class StatRuleTypeController extends BaseController<
        StatRuleType,
        StatRuleTypeDto,
        StatRuleTypeService> {

    private final ExportExcel exportExcel;

    public StatRuleTypeController(StatRuleTypeService service, ExportExcel exportExcel) {
        super(service);
        this.exportExcel = exportExcel;
    }

    @LogActivity(function = "Tìm kiếm loại quy tắc")
    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<ResponseSearchStatRuleTypeDto>>> search(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseData.okEntity(service.search(search, pageable));
    }

    @LogActivity(function = "Xuất Excel loại quy tắc")
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam String search,
                                              @RequestParam String type, Pageable pageable
    ) throws Exception {
        if (type.equals("all")) {
            pageable = Pageable.unpaged();
        }
        Page<ResponseSearchStatRuleTypeDto> data = service.search(search , pageable);
        List<ResponseSearchStatRuleTypeDto> dataList = data.getContent();
        return exportExcel.exportExcel(dataList, "Thông_tin_loại_quy_tắc.xlsx");
    }
}
