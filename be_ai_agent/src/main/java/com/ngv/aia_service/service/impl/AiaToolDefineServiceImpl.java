package com.ngv.aia_service.service.impl;

import com.ngv.aia_service.dto.request.AiaToolDefineRequest;
import com.ngv.aia_service.dto.response.AiaToolDefineResponse;
import com.ngv.aia_service.model.entity.AiaToolDefine;
import com.ngv.aia_service.repository.AiaToolDefineRepository;
import com.ngv.aia_service.service.AiaToolDefineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AiaToolDefineServiceImpl implements AiaToolDefineService {

    private final AiaToolDefineRepository aiaToolDefineRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AiaToolDefineResponse> searchAll(String filter, Pageable pageable) {
        log.info("Searching all tools with filter: {}", filter);
        return aiaToolDefineRepository.searchAll(filter, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AiaToolDefineResponse> findAllActiveTool() {
        log.info("Finding all active tools");
        return aiaToolDefineRepository.findAllActiveTool();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AiaToolDefine> findById(Long toolId) {
        log.info("Finding tool by ID: {}", toolId);
        return aiaToolDefineRepository.findById(toolId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AiaToolDefineResponse> findDetailById(Long toolId) {
        log.info("Finding tool detail by ID: {}", toolId);
        return aiaToolDefineRepository.findDetailById(toolId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AiaToolDefine> findByActionNameAndVersion(String actionName, String version) {
        log.info("Finding tool by actionName: {} and version: {}", actionName, version);
        return aiaToolDefineRepository.findByActionNameAndVersion(actionName, version);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AiaToolDefineResponse> findByCategoryCode(String categoryCode) {
        log.info("Finding tools by categoryCode: {}", categoryCode);
        return aiaToolDefineRepository.findByCategoryCode(categoryCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AiaToolDefine> findByBackendActionCode(String backendActionCode) {
        log.info("Finding tool by backendActionCode: {}", backendActionCode);
        return aiaToolDefineRepository.findByBackendActionCode(backendActionCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AiaToolDefineResponse> findByToolType(String toolType) {
        log.info("Finding tools by toolType: {}", toolType);
        return aiaToolDefineRepository.findByToolType(toolType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AiaToolDefineResponse> searchWithCriteria(String categoryCode, String toolType, 
                                                         Integer isActive, String keyword, Pageable pageable) {
        log.info("Searching tools with criteria - categoryCode: {}, toolType: {}, isActive: {}, keyword: {}", 
                categoryCode, toolType, isActive, keyword);
        return aiaToolDefineRepository.searchWithCriteria(categoryCode, toolType, isActive, keyword, pageable);
    }

    @Override
    public AiaToolDefine createTool(AiaToolDefineRequest request) {
        log.info("Creating new tool with actionName: {}", request.getActionName());
        
        // Kiểm tra tồn tại actionName và version
        if (existsByActionNameAndVersion(request.getActionName(), request.getVersion())) {
            throw new RuntimeException("Tool với actionName '" + request.getActionName() + 
                                     "' và version '" + request.getVersion() + "' đã tồn tại");
        }
        
        AiaToolDefine tool = mapRequestToEntity(request);
        tool.setCreatedDate(LocalDateTime.now());
        
        return aiaToolDefineRepository.save(tool);
    }

    @Override
    public AiaToolDefine updateTool(Long toolId, AiaToolDefineRequest request) {
        log.info("Updating tool with ID: {}", toolId);
        
        AiaToolDefine existingTool = findById(toolId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tool với ID: " + toolId));
        
        // Kiểm tra tồn tại actionName và version khi update
        if (checkActionNameVersionExistForUpdate(request.getActionName(), request.getVersion(), toolId)) {
            throw new RuntimeException("Tool với actionName '" + request.getActionName() + 
                                     "' và version '" + request.getVersion() + "' đã tồn tại");
        }
        
        updateEntityFromRequest(existingTool, request);
        existingTool.setModifiedDate(LocalDateTime.now());
        
        return aiaToolDefineRepository.save(existingTool);
    }

    @Override
    public void deleteTool(Long toolId) {
        log.info("Deleting tool with ID: {}", toolId);
        
        AiaToolDefine tool = findById(toolId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tool với ID: " + toolId));
        
        tool.setIsActive(0);
        tool.setModifiedDate(LocalDateTime.now());
        aiaToolDefineRepository.save(tool);
    }

    @Override
    public void toggleToolStatus(Long toolId) {
        log.info("Toggling status for tool with ID: {}", toolId);
        
        AiaToolDefine tool = findById(toolId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tool với ID: " + toolId));
        
        tool.setIsActive(tool.getIsActive() == 1 ? 0 : 1);
        tool.setModifiedDate(LocalDateTime.now());
        aiaToolDefineRepository.save(tool);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByActionNameAndVersion(String actionName, String version) {
        return aiaToolDefineRepository.existsByActionNameAndVersion(actionName, version);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkActionNameVersionExistForUpdate(String actionName, String version, Long toolId) {
        Integer count = aiaToolDefineRepository.checkActionNameVersionExistForUpdate(actionName, version, toolId);
        return count != null && count > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AiaToolDefineResponse> findAllForExport() {
        log.info("Finding all tools for export");
        return aiaToolDefineRepository.findAllForExport();
    }

    private AiaToolDefine mapRequestToEntity(AiaToolDefineRequest request) {
        AiaToolDefine tool = new AiaToolDefine();
        tool.setActionName(request.getActionName());
        tool.setDisplayName(request.getDisplayName());
        tool.setCategoryCode(request.getCategoryCode());
        tool.setToolType(request.getToolType() != null ? request.getToolType() : "API_CALL");
        tool.setToolDescription(request.getToolDescription());
        tool.setPreconditions(request.getPreconditions());
        tool.setUserParamSchema(request.getUserParamSchema());
        tool.setOutputSchema(request.getOutputSchema());
        tool.setChainingHints(request.getChainingHints());
        tool.setErrorHandlingHints(request.getErrorHandlingHints());
        tool.setUsageExamples(request.getUsageExamples());
        tool.setBackendActionCode(request.getBackendActionCode());
        tool.setSystemParam(request.getSystemParam());
        tool.setVersion(request.getVersion() != null ? request.getVersion() : "1.0");
        tool.setIsActive(request.getIsActive() != null ? request.getIsActive() : 1);
        tool.setCreatedBy(request.getCreatedBy());
        return tool;
    }

    private void updateEntityFromRequest(AiaToolDefine tool, AiaToolDefineRequest request) {
        tool.setActionName(request.getActionName());
        tool.setDisplayName(request.getDisplayName());
        tool.setCategoryCode(request.getCategoryCode());
        tool.setToolType(request.getToolType());
        tool.setToolDescription(request.getToolDescription());
        tool.setPreconditions(request.getPreconditions());
        tool.setUserParamSchema(request.getUserParamSchema());
        tool.setOutputSchema(request.getOutputSchema());
        tool.setChainingHints(request.getChainingHints());
        tool.setErrorHandlingHints(request.getErrorHandlingHints());
        tool.setUsageExamples(request.getUsageExamples());
        tool.setBackendActionCode(request.getBackendActionCode());
        tool.setSystemParam(request.getSystemParam());
        tool.setVersion(request.getVersion());
        tool.setIsActive(request.getIsActive());
        tool.setModifiedBy(request.getModifiedBy());
    }
}
