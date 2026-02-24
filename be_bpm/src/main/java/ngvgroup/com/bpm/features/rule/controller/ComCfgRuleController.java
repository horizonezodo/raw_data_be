package ngvgroup.com.bpm.features.rule.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ngvgroup.com.bpm.features.rule.dto.InfUserDto;
import ngvgroup.com.bpm.features.rule.dto.ResponseRuleDto;
import ngvgroup.com.bpm.features.rule.dto.RuleDTO;
import ngvgroup.com.bpm.features.rule.dto.RuleExcelDto;
import ngvgroup.com.bpm.features.rule.service.ComCfgRuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rule")
//@PreAuthorize("hasRole('rule_admin')")
public class ComCfgRuleController {
    private final ComCfgRuleService comCfgRuleService;
    private final ExportExcel excel;

    public ComCfgRuleController(ComCfgRuleService comCfgRuleService, ExportExcel excel) {
        this.comCfgRuleService = comCfgRuleService;
        this.excel = excel;
    }

    @Operation(
            summary = "Lấy danh sách Rule",
            description = "Lấy danh sách Rule với điều kiện active = 1 , phân trang",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công")
            })
    @PostMapping
    public ResponseEntity<ResponseData<Page<RuleDTO>>> getRules(@RequestBody RuleDTO dto, Pageable pageable) {
        Page<RuleDTO> pageData = comCfgRuleService.getRules(dto, pageable);
        return ResponseData.okEntity(pageData);
    }


    @Operation(
            summary = "Tìm kiếm gần chính xác Rule",
            description = "Lấy danh sách Rule với điều kiện active = 1 , phân trang và tìm kiếm từ DTO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @GetMapping("/search-rule")
    public ResponseEntity<ResponseData<Page<ResponseRuleDto>>> searchRule(@RequestParam String keyword, Pageable pageable) {
        Page<ResponseRuleDto> pageData = comCfgRuleService.searchRule(keyword, pageable);
        return ResponseData.okEntity(pageData);
    }

    @Operation(
            summary = "Xem chi tiết của  Rule",
            description = "Xem thông tin chi tiết của rule chia bài ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy dữ liệu")
            })
    @GetMapping("/{ruleCode}")
    public ResponseEntity<ResponseData<RuleDTO>> ruleDetail(@PathVariable("ruleCode") String ruleCode) {
        RuleDTO ruleDTO = comCfgRuleService.getRule(ruleCode);
        return ResponseData.okEntity(ruleDTO);
    }

    @Operation(
            summary = "Tạo mới 1 Rule",
            description = "Tạo mới 1 rule ",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Thành công"),
                    @ApiResponse(responseCode = "409", description = "Mã rule code bị trùng"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @PostMapping("/create-rule")
    public ResponseEntity<ResponseData<Void>> createRule(@RequestBody RuleDTO ruleDTO) {
        comCfgRuleService.createRule(ruleDTO);
        return ResponseData.createdEntity();
    }

    @Operation(
            summary = "Cập nhật thông tin trong Rule",
            description = "Cập nhật danh sách nhân viên trong 1 rule",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy dữ liệu"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @PutMapping("/update-user-from-rule/{ruleCode}")
    public ResponseEntity<ResponseData<Void>> addUserToRule(@PathVariable("ruleCode") String ruleCode, @RequestBody RuleDTO dto) {
        comCfgRuleService.updateUserFromRule(ruleCode, dto);
        return ResponseData.okEntity(null);
    }

    @Operation(
            summary = "Xuất file excel",
            description = "Tạo 1 file excel chứa các giá trị trong rule table",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable String fileName, @RequestBody RuleDTO rq, Pageable pageable) throws Exception {
        List<RuleExcelDto> data = comCfgRuleService.exportToExcel(rq, pageable);
        return excel.exportExcel(data, fileName);
    }

    @Operation(
            summary = "Xóa bỏ 1 Rule",
            description = "Đổi trạng thái của 1 rule",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @DeleteMapping("/remove-rule/{ruleCode}")
    public ResponseEntity<ResponseData<Void>> removeRule(@PathVariable("ruleCode") String ruleCode) {
        comCfgRuleService.deleteRule(ruleCode);
        return ResponseData.okEntity(null);
    }


    @Operation(
            summary = "Lấy danh sách Rule theo userId",
            description = "Lấy danh sách rule theo userId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @GetMapping("/rule-by-userId/{userCode}")
    public ResponseEntity<ResponseData<List<RuleDTO>>> listRule(@PathVariable("userCode") String userCode) {
        List<RuleDTO> ruleDTOList = comCfgRuleService.getAllRule(userCode);
        return ResponseData.okEntity(ruleDTOList);
    }

    @Operation(
            summary = "Tìm kiếm rule theo nhiều điều kiện",
            description = "Tìm kiếm theo điều kiện nâng cao và theo giá trị tìm kiếm",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<RuleDTO>>> searchAll(@RequestBody RuleDTO dto, Pageable pageable) {
        Page<RuleDTO> ruleDTOS = comCfgRuleService.searchAll(dto, pageable);
        return ResponseData.okEntity(ruleDTOS);
    }

    @Operation(
            summary = "Lấy danh sách user theo rule code"
    )
    @GetMapping("{ruleCode}/users")
    @PreAuthorize("hasAnyRole('admin_rule', 'admin_task_monitor')")
    public ResponseEntity<ResponseData<List<InfUserDto>>> getAllRuleUserByRuleCode(@PathVariable("ruleCode") String ruleCode) {
        return ResponseData.okEntity(comCfgRuleService.getAllRuleUserByRuleCode(ruleCode));
    }

    @Operation(
            summary = "Lấy danh sách user "
    )
    @GetMapping("/users")
    public ResponseEntity<ResponseData<List<InfUserDto>>> getUser() {
        return ResponseData.okEntity(comCfgRuleService.listUser());
    }

}
