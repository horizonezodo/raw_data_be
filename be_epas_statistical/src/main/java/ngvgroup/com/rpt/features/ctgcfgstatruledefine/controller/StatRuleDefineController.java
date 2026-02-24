package ngvgroup.com.rpt.features.ctgcfgstatruledefine.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.ReqSearchStatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.ResponseSearchStatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.StatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.model.StatRuleDefine;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.service.StatRuleDefineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stat-rule-define")
public class StatRuleDefineController extends BaseController<
        StatRuleDefine,
        StatRuleDefineDto,
        StatRuleDefineService
        > {

    private final ExportExcel exportExcel;

    public StatRuleDefineController(StatRuleDefineService service, ExportExcel exportExcel) {
        super(service);
        this.exportExcel = exportExcel;
    }


    @LogActivity(function = "Tìm kiếm định nghĩa quy tắc")
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<ResponseSearchStatRuleDefineDto>>> search(
            @RequestParam(required = false) String search,
            @RequestBody(required = false) ReqSearchStatRuleDefineDto body,
            Pageable pageable) {
        return ResponseData.okEntity(service.search(search, body, pageable));
    }

    @LogActivity(function = "Xuất Excel định nghĩa quy tắc")
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam String search,
                                              @RequestBody ReqSearchStatRuleDefineDto body,
                                              @RequestParam String type, Pageable pageable
    ) throws Exception {
        if (type.equals("all")) {
            pageable = Pageable.unpaged();
        }
        Page<ResponseSearchStatRuleDefineDto> data = service.search(search ,body, pageable);
        List<ResponseSearchStatRuleDefineDto> dataList = data.getContent();
        return exportExcel.exportExcel(dataList, "Thông_tin_định_nghĩa_quy_tắc.xlsx");
    }
}
