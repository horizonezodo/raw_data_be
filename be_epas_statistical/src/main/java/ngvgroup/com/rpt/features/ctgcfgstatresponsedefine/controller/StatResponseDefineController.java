package ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.web.controller.BaseController;

import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.ResponseSearchStatResponseDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.StatResponseDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.model.StatResponseDefine;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.service.StatResponseDefineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stat-response-define")
public class StatResponseDefineController extends BaseController<
        StatResponseDefine,
        StatResponseDefineDto,
        StatResponseDefineService> {

    private final ExportExcel exportExcel;

    public StatResponseDefineController(StatResponseDefineService service, ExportExcel exportExcel) {
        super(service);
        this.exportExcel = exportExcel;
    }

    @LogActivity(function = "Tìm kiếm định nghĩa phản hồi")
    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<ResponseSearchStatResponseDefineDto>>> search(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseData.okEntity(service.search(search, pageable));
    }

    @LogActivity(function = "Xuất Excel định nghĩa phản hồi")
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam String search, @RequestParam String type, Pageable pageable) throws Exception {
        if (type.equals("all")) {
            pageable = Pageable.unpaged();
        }
        Page<ResponseSearchStatResponseDefineDto> data = service.search(search , pageable);
        List<ResponseSearchStatResponseDefineDto> dataList = data.getContent();
        return exportExcel.exportExcel(dataList, "StatResponseDefine.xlsx");
    }
}
