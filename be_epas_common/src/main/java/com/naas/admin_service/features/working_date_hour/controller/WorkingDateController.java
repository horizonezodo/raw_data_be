package com.naas.admin_service.features.working_date_hour.controller;

import com.naas.admin_service.features.working_date_hour.dto.working_date.ComCfgSysWorkingDateDto;
import com.naas.admin_service.features.working_date_hour.dto.working_date.WorkingDateResDto;
import com.naas.admin_service.features.working_date_hour.dto.working_date.WorkingDateSearchRequest;
import com.naas.admin_service.features.working_date_hour.dto.working_date.YearInitDto;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingDate;
import com.naas.admin_service.features.working_date_hour.service.ComCfgSysWorkingDateService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogActivity(function = "Danh mục ngày làm việc")
@RestController
@RequestMapping("/calendar")
public class WorkingDateController extends BaseController<ComCfgSysWorkingDate, WorkingDateResDto, ComCfgSysWorkingDateService> {

    public WorkingDateController(ComCfgSysWorkingDateService service) {
        super(service);
    }

    @LogActivity(function = "Chức năng lấy thông tin lịch làm việc")
    @PostMapping("/search")
    public ResponseEntity<ResponseData<List<WorkingDateResDto>>> searchWorkingDate(
            @RequestBody WorkingDateSearchRequest request) {

        return ResponseData.okEntity(service.searchWorkingDate(request));
    }

    @LogActivity(function = "Chức năng đăng ký ngày làm bù")
    @PostMapping("/register")
    public ResponseEntity<ResponseData<String>> registerWorkingDate(@RequestBody ComCfgSysWorkingDateDto request) {
        service.registerWorkingDate(request);
        return ResponseData.okEntity("Đăng ký thành công.");
    }

    @LogActivity(function = "Chức năng hủy đăng ký ngày làm bù")
    @PostMapping("/abort")
    public ResponseEntity<ResponseData<String>> removeWorkingDate(@RequestBody ComCfgSysWorkingDateDto request) {
        service.removeWorkingDate(request);
        return ResponseData.okEntity("Hủy đăng ký thành công.");
    }

    @LogActivity(function = "Chức năng khởi tạo lịch năm")
    @PostMapping("/init-year")
    public ResponseEntity<Void> initYear(@RequestBody @Valid YearInitDto request) {
        service.inItWorkingYear(request);
        return ResponseEntity.ok().build();
    }

}
