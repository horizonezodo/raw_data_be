package ngvgroup.com.bpmn.controller;

import com.ngvgroup.bpm.core.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ngvgroup.com.bpmn.dto.RuleDTO.RuleDTO;
import ngvgroup.com.bpmn.dto.SearchDTO.SearchDTO;
import ngvgroup.com.bpmn.dto.UserDto.InfUserDto;
import ngvgroup.com.bpmn.service.ComCfgRuleService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rule")
@PreAuthorize("hasRole('admin_rule')")
public class ComCfgRuleController {
    private final ComCfgRuleService comCfgRuleService;

    public ComCfgRuleController(ComCfgRuleService comCfgRuleService) {
        this.comCfgRuleService = comCfgRuleService;
    }

    @Operation(
            summary = "Lấy danh sách Rule",
            description = "Lấy danh sách Rule với điều kiện active = 1 , phân trang",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công")
            })
    @PostMapping
    public ResponseEntity<ResponseData<Page<RuleDTO>>> getRules(@RequestBody RuleDTO dto){
        Page<RuleDTO> pageData = comCfgRuleService.getRules(dto);
        return ResponseData.okEntity(pageData);
    }


    @Operation(
            summary = "Tìm kiếm gần chính xác Rule",
            description = "Lấy danh sách Rule với điều kiện active = 1 , phân trang và tìm kiếm từ DTO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @PostMapping("/search-rule")
    public ResponseEntity<ResponseData<Page<RuleDTO>>> searchRule(@RequestBody SearchDTO searchDTO){
        Page<RuleDTO> pageData = comCfgRuleService.searchRule(searchDTO);
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
    public ResponseEntity<ResponseData<RuleDTO>> ruleDetail(@PathVariable("ruleCode")String ruleCode) {
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
    public ResponseEntity<ResponseData<Void>> addUserToRule(@PathVariable("ruleCode")String ruleCode,@RequestBody RuleDTO dto){
        comCfgRuleService.updateUserFromRule(ruleCode,dto);
        return ResponseData.okEntity();
    }

    @Operation(
            summary = "Xuất file excel",
            description = "Tạo 1 file excel chứa các giá trị trong rule table",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@PathVariable String fileName, @RequestBody RuleDTO rq) {
        return comCfgRuleService.exportToExcel(rq,fileName);
    }

    @Operation(
            summary = "Xóa bỏ 1 Rule",
            description = "Đổi trạng thái của 1 rule",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @DeleteMapping("/remove-rule/{ruleCode}")
    public ResponseEntity<ResponseData<Void>> removeRule(@PathVariable("ruleCode")String ruleCode) {
        comCfgRuleService.deleteRule(ruleCode);
        return ResponseData.okEntity();
    }

    @Operation(
            summary = "Lấy danh sách Rule theo userId",
            description = "Lấy danh sách rule theo userId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
            })
    @GetMapping("/rule-by-userId/{userCode}")
    public ResponseEntity<ResponseData<List<RuleDTO>>> listRule(@PathVariable("userCode")String userCode){
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
    public ResponseEntity<ResponseData<Page<RuleDTO>>> searchAll(@RequestBody RuleDTO dto){
        Page<RuleDTO> ruleDTOS = comCfgRuleService.searchAll(dto);
        return ResponseData.okEntity(ruleDTOS);
    }

//    @PutMapping("/change-parent-code/{ruleCode}")
//    public ResponseEntity<ResponseData<?>> changeParentCode(@PathVariable("ruleCode")String ruleCode,@RequestBody RuleDTO ruleDTO) throws ValidationException, DuplicateException {
//        ruleService.updateParentCode(ruleCode,ruleDTO);
//        return ResponseData.okEntity(null);
//    }

    @Operation(
            summary = "Lấy danh sách user theo rule code"
    )
    @GetMapping("{ruleCode}/users")
    @PreAuthorize("hasAnyRole('admin_rule', 'admin_task_monitor')")
    public ResponseEntity<ResponseData<List<InfUserDto>>> getAllRuleUserByRuleCode(@PathVariable String ruleCode) {
        return ResponseData.okEntity(comCfgRuleService.getAllRuleUserByRuleCode(ruleCode));
    }
}
