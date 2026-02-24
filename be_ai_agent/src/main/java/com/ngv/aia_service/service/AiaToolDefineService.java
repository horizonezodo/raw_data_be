package com.ngv.aia_service.service;

import com.ngv.aia_service.dto.request.AiaToolDefineRequest;
import com.ngv.aia_service.dto.response.AiaToolDefineResponse;
import com.ngv.aia_service.model.entity.AiaToolDefine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AiaToolDefineService {

    /**
     * Tìm kiếm tất cả tool với filter
     */
    Page<AiaToolDefineResponse> searchAll(String filter, Pageable pageable);

    /**
     * Lấy danh sách tool đang active
     */
    List<AiaToolDefineResponse> findAllActiveTool();

    /**
     * Tìm tool theo ID
     */
    Optional<AiaToolDefine> findById(Long toolId);

    /**
     * Lấy chi tiết tool theo ID
     */
    Optional<AiaToolDefineResponse> findDetailById(Long toolId);

    /**
     * Tìm tool theo actionName và version
     */
    Optional<AiaToolDefine> findByActionNameAndVersion(String actionName, String version);

    /**
     * Tìm tool theo category code
     */
    List<AiaToolDefineResponse> findByCategoryCode(String categoryCode);

    /**
     * Tìm tool theo backend action code
     */
    Optional<AiaToolDefine> findByBackendActionCode(String backendActionCode);

    /**
     * Tìm tool theo tool type
     */
    List<AiaToolDefineResponse> findByToolType(String toolType);

    /**
     * Tìm kiếm với nhiều điều kiện
     */
    Page<AiaToolDefineResponse> searchWithCriteria(String categoryCode, String toolType, 
                                                  Integer isActive, String keyword, Pageable pageable);

    /**
     * Tạo mới tool
     */
    AiaToolDefine createTool(AiaToolDefineRequest request);

    /**
     * Cập nhật tool
     */
    AiaToolDefine updateTool(Long toolId, AiaToolDefineRequest request);

    /**
     * Xóa tool (soft delete)
     */
    void deleteTool(Long toolId);

    /**
     * Chuyển đổi trạng thái active/inactive
     */
    void toggleToolStatus(Long toolId);

    /**
     * Kiểm tra tồn tại actionName và version
     */
    boolean existsByActionNameAndVersion(String actionName, String version);

    /**
     * Kiểm tra actionName và version khi update
     */
    boolean checkActionNameVersionExistForUpdate(String actionName, String version, Long toolId);

    /**
     * Lấy tất cả tool để export
     */
    List<AiaToolDefineResponse> findAllForExport();
}
