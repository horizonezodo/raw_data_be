package ngvgroup.com.rpt.features.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonResponse;
import ngvgroup.com.rpt.features.common.service.ComCfgCommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class ComCfgCommonController {
    private final ComCfgCommonService service;

    @LogActivity(function = "Lấy danh sách STAT_COMMON theo COMMON_TYPE_CODE")
    @Operation(summary = "Lấy danh sách STAT_COMMON theo COMMON_TYPE_CODE")
    @GetMapping
    public ResponseEntity<ResponseData<List<ComCfgCommonDto>>> listCommonByCommonTypeCode(@RequestParam String commonTypeCode) {
        return ResponseData.okEntity(this.service.listCommonByCommonTypeCode(commonTypeCode));
    }

    @LogActivity(function = "Lấy tất cả cấu hình chung")
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<ComCfgCommonDto>>> getAll() {

        return ResponseData.okEntity(service.getAllBy());
    }

    @LogActivity(function = "Lấy cấu hình chung theo mã loại")
    @GetMapping("get-by-common-type-code")
    public ResponseEntity<ResponseData<List<CommonResponse>>> getCommonByCommonTypeCode(@RequestParam String commonTypeCode) {
        List<CommonResponse> response = service.getAllCommonByCommonTypeCode(commonTypeCode);
        return ResponseData.okEntity(response);
    }

    @LogActivity(function = "Lấy cấu hình chung theo mã cha")
    @GetMapping("/get-all-by-parent")
    public ResponseEntity<ResponseData<List<ComCfgCommonDto>>> getCommonByParentCode(@RequestParam String parentCode) {
        return ResponseData.okEntity(service.getAllCommonByParentCode(parentCode));
    }

    @LogActivity(function = "Lấy cấu hình chung theo danh sách mã loại")
    @GetMapping("/get-in-list-common")
    public ResponseEntity<ResponseData<List<ComCfgCommonDto>>>getAllByListCommonTypeCode(@RequestParam List<String> commonTypeCodes){
        return  ResponseData.okEntity(service.getAllByListCommonTypeCode(commonTypeCodes));
    }

    @LogActivity(function = "Lấy cấu hình chung theo mã cha và mã loại")
    @GetMapping("/get-by-parent-type")
    public ResponseEntity<ResponseData<List<ComCfgCommonDto>>>getAllByParentCodeAndTypeCode(
                                                        @RequestParam(name = "parentCode", required = false) String parentCode,
                                                        @RequestParam("commonTypeCode") String commonTypeCode){
        return  ResponseData.okEntity(service.getAllByParentCodeAndTypeCode(parentCode, commonTypeCode));
    }
}
