package com.ngv.aia_service.controller;

import com.ngv.aia_service.dto.request.AiaToolDefineRequest;
import com.ngv.aia_service.dto.request.PageableRequest;
import com.ngv.aia_service.dto.response.AiaToolDefineResponse;
import com.ngv.aia_service.dto.response.ResponseData;
import com.ngv.aia_service.model.entity.AiaToolDefine;
import com.ngv.aia_service.service.AiaToolDefineService;
import com.ngv.aia_service.util.page.PageUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aia/tools")
@RequiredArgsConstructor
@Slf4j
public class AiaToolDefineController {

    private final AiaToolDefineService aiaToolDefineService;

    /**
     * Lấy danh sách tool với phân trang và tìm kiếm
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_VIEW)")
    @GetMapping
    public ResponseEntity<ResponseData<Page<AiaToolDefineResponse>>> getTools(
            @RequestParam(required = false) String filter,
            @ModelAttribute PageableRequest pageableRequest) {
        log.info("Getting tools with filter: {}", filter);
        
        Pageable pageable = PageUtils.toPageable(pageableRequest);
        Page<AiaToolDefineResponse> tools = aiaToolDefineService.searchAll(filter, pageable);
        
        return ResponseData.okEntity(tools);
    }

    /**
     * Lấy danh sách tool đang active
     */
    // @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_VIEW)")
    @GetMapping("/active")
    public ResponseEntity<ResponseData<List<AiaToolDefineResponse>>> getActiveTools() {
        log.info("Getting all active tools");
        
        List<AiaToolDefineResponse> tools = aiaToolDefineService.findAllActiveTool();
        
        return ResponseData.okEntity(tools);
    }

    /**
     * Lấy chi tiết tool theo ID
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_VIEW)")
    @GetMapping("/{toolId}")
    public ResponseEntity<ResponseData<AiaToolDefineResponse>> getToolDetail(@PathVariable Long toolId) {
        log.info("Getting tool detail for ID: {}", toolId);
        
        return aiaToolDefineService.findDetailById(toolId)
                .map(ResponseData::okEntity)
                .orElse(ResponseData.notFoundEntity("Tool not found"));
    }

    /**
     * Tìm kiếm tool với nhiều điều kiện
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_VIEW)")
    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<AiaToolDefineResponse>>> searchTools(
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String toolType,
            @RequestParam(required = false) Integer isActive,
            @RequestParam(required = false) String keyword,
            @ModelAttribute PageableRequest pageableRequest) {
        log.info("Searching tools with criteria - categoryCode: {}, toolType: {}, isActive: {}, keyword: {}", 
                categoryCode, toolType, isActive, keyword);
        
        Pageable pageable = PageUtils.toPageable(pageableRequest);
        Page<AiaToolDefineResponse> tools = aiaToolDefineService.searchWithCriteria(
                categoryCode, toolType, isActive, keyword, pageable);
        
        return ResponseData.okEntity(tools);
    }

    /**
     * Lấy tool theo category code
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_VIEW)")
    @GetMapping("/category/{categoryCode}")
    public ResponseEntity<ResponseData<List<AiaToolDefineResponse>>> getToolsByCategory(@PathVariable String categoryCode) {
        log.info("Getting tools by category: {}", categoryCode);
        
        List<AiaToolDefineResponse> tools = aiaToolDefineService.findByCategoryCode(categoryCode);
        
        return ResponseData.okEntity(tools);
    }

    /**
     * Lấy tool theo tool type
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_VIEW)")
    @GetMapping("/type/{toolType}")
    public ResponseEntity<ResponseData<List<AiaToolDefineResponse>>> getToolsByType(@PathVariable String toolType) {
        log.info("Getting tools by type: {}", toolType);
        
        List<AiaToolDefineResponse> tools = aiaToolDefineService.findByToolType(toolType);
        
        return ResponseData.okEntity(tools);
    }

    /**
     * Tạo mới tool
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_EDIT)")
    @PostMapping
    public ResponseEntity<ResponseData<AiaToolDefine>> createTool(@Valid @RequestBody AiaToolDefineRequest request) {
        log.info("Creating new tool with actionName: {}", request.getActionName());
        
        try {
            AiaToolDefine tool = aiaToolDefineService.createTool(request);
            return ResponseData.okEntity(tool);
        } catch (Exception e) {
            log.error("Error creating tool", e);
            return ResponseData.errorEntity(e.getMessage());
        }
    }

    /**
     * Cập nhật tool
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_EDIT)")
    @PutMapping("/{toolId}")
    public ResponseEntity<ResponseData<AiaToolDefine>> updateTool(@PathVariable Long toolId, 
                                                                @Valid @RequestBody AiaToolDefineRequest request) {
        log.info("Updating tool with ID: {}", toolId);
        
        try {
            AiaToolDefine tool = aiaToolDefineService.updateTool(toolId, request);
            return ResponseData.okEntity(tool);
        } catch (Exception e) {
            log.error("Error updating tool", e);
            return ResponseData.errorEntity(e.getMessage());
        }
    }

    /**
     * Xóa tool (soft delete)
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_DELETE)")
    @DeleteMapping("/{toolId}")
    public ResponseEntity<ResponseData<Void>> deleteTool(@PathVariable Long toolId) {
        log.info("Deleting tool with ID: {}", toolId);
        
        try {
            aiaToolDefineService.deleteTool(toolId);
            return ResponseData.okEntity();
        } catch (Exception e) {
            log.error("Error deleting tool", e);
            return ResponseData.errorEntity(e.getMessage());
        }
    }

    /**
     * Chuyển đổi trạng thái tool
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_EDIT)")
    @PutMapping("/{toolId}/toggle-status")
    public ResponseEntity<ResponseData<Void>> toggleToolStatus(@PathVariable Long toolId) {
        log.info("Toggling status for tool with ID: {}", toolId);
        
        try {
            aiaToolDefineService.toggleToolStatus(toolId);
            return ResponseData.okEntity();
        } catch (Exception e) {
            log.error("Error toggling tool status", e);
            return ResponseData.errorEntity(e.getMessage());
        }
    }

    /**
     * Export tất cả tool
     */
    @PreAuthorize("hasRole(T(com.ngv.aia_service.constant.Roles).USER_VIEW)")
    @GetMapping("/export")
    public ResponseEntity<ResponseData<List<AiaToolDefineResponse>>> exportTools() {
        log.info("Exporting all tools");
        
        List<AiaToolDefineResponse> tools = aiaToolDefineService.findAllForExport();
        
        return ResponseData.okEntity(tools);
    }
}
