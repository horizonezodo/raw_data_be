package ngvgroup.com.rpt.features.ctgcfgstattemplate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto;
import ngvgroup.com.rpt.features.common.dto.CommonResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stat-group")
@RequiredArgsConstructor
@Tag(name = "CtgCfgStatGroup", description = "Danh sách nhóm thống kê từ CTG_CFG_STAT_COMMON")
public class CtgCfgStatGroupController {
    private final CtgCfgStatGroupService statGroupService;

    // Example: /stat-group/get-by-type?typeCode=CM002
    @LogActivity(function = "Lấy nhóm thống kê theo loại")
    @GetMapping("/get-by-type")
    @Operation(summary = "Lấy nhóm thống kê theo COMMON_TYPE_CODE", description = "Trả về các bản ghi active từ CTG_CFG_STAT_COMMON theo COMMON_TYPE_CODE (ví dụ: CM002)")
    public ResponseEntity<ResponseData<List<ComCfgCommonDto>>> getGroups(
            @Parameter(description = "COMMON_TYPE_CODE, ví dụ: CM002") @RequestParam("typeCode") String typeCode) {
        return ResponseData.okEntity(statGroupService.getGroupsByType(typeCode));
    }

    // Example: /stat-group/get-menu-type-code
    @LogActivity(function = "Lấy danh sách nhóm thống kê cho menu")
    @GetMapping("/get-menu-type-code")
    @Operation(summary = "Lấy danh sách nhóm thống kê cho menu", description = "Trả về các nhóm thống kê từ CTG_CFG_STAT_COMMON có dữ liệu thực tế trong CTG_CFG_STAT_TYPE với COMMON_TYPE_CODE = 'CM002'")
    public ResponseEntity<ResponseData<List<CommonResponse>>> getMenuTypeCode() {
        return ResponseData.okEntity(statGroupService.getGroupsWithData("CM093"));
    }
}
