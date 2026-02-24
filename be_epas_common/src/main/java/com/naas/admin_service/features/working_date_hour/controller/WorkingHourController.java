package com.naas.admin_service.features.working_date_hour.controller;

import com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourDto;
import com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourExcel;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingHour;
import com.naas.admin_service.features.working_date_hour.service.ComCfgSysWorkingHourService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogActivity(function = "Danh mục giờ làm việc")
@RestController
@RequestMapping("/sys-working-hours")
public class WorkingHourController extends BaseController<
        ComCfgSysWorkingHour,
        SysWorkingHourDto,
        ComCfgSysWorkingHourService> {
    private final ExportExcel exportExcel;


    public WorkingHourController(ComCfgSysWorkingHourService service, ExportExcel exportExcel) {
        super(service);
        this.exportExcel = exportExcel;
    }

    @GetMapping("/search")
    @LogActivity(function = "Chức năng lấy tất cả thông tin giờ làm việc")
    public ResponseEntity<ResponseData<Page<SysWorkingHourDto>>> search(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return ResponseData.okEntity(service.search(keyword, pageable));
    }

    @PostMapping("add")
    @LogActivity(function = "Chức năng thêm mới giờ làm việc")
    public ResponseEntity<ResponseData<List<ComCfgSysWorkingHour>>> create(@Valid @RequestBody List<SysWorkingHourDto> dto) {
        return ResponseData.okEntity(service.create(dto));
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam String keyword,
            @RequestParam String fileName,
            Pageable pageable) throws Exception {
        List<SysWorkingHourExcel> data = service.exportData(keyword, pageable);
        return exportExcel.exportExcel(data, fileName);
    }

}
