package com.naas.admin_service.features.working_date_hour.controller;

import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayDTO;
import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayResDTO;
import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidaySearch;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysHolidayDate;
import com.naas.admin_service.features.working_date_hour.service.HolidayService;
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

@LogActivity(function = "Ngày nghỉ")
@RestController
@RequestMapping("/holiday")
public class HolidayController extends BaseController<
        ComCfgSysHolidayDate,
        HolidayDTO,
        HolidayService
        > {

    private final ExportExcel exportExcel;

    public HolidayController(HolidayService service, ExportExcel exportExcel) {
        super(service);
        this.exportExcel = exportExcel;
    }


    @LogActivity(function = "Thông tin ngày nghỉ")
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<HolidayResDTO>>> search(
            @RequestBody HolidaySearch request,
            Pageable pageable) {
        return ResponseData.okEntity(service.search(request, pageable));
    }

    @PostMapping("/add-all")
    @LogActivity(function = "Thêm mới ngày nghỉ")
    public ResponseEntity<ResponseData<List<HolidayDTO>>> addListHolidays(@Valid @RequestBody List<HolidayDTO> dtos) {
        return ResponseData.okEntity(service.addAll(dtos));
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestBody HolidaySearch request,
            Pageable pageable) throws Exception {
        Page<HolidayResDTO> page = service.exportData(request, pageable);
        List<HolidayResDTO> data = page.getContent();
        return exportExcel.exportExcel(data, "Danh sach ngay nghi");
    }
}
