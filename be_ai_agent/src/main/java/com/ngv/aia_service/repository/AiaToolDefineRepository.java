package com.ngv.aia_service.repository;

import com.ngv.aia_service.model.entity.AiaToolDefine;
import com.ngv.aia_service.dto.response.AiaToolDefineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiaToolDefineRepository extends JpaRepository<AiaToolDefine, Long> {

    @Query("SELECT new com.ngv.aia_service.dto.response.AiaToolDefineResponse(" +
           "a.toolId, a.actionName, a.displayName, a.categoryCode, a.toolType, " +
           "a.toolDescription, a.preconditions, a.userParamSchema, a.outputSchema, " +
           "a.chainingHints, a.errorHandlingHints, a.usageExamples, a.backendActionCode, " +
           "a.systemParam, a.version, a.isActive, a.createdDate, a.createdBy, " +
           "a.modifiedDate, a.modifiedBy) " +
           "FROM AiaToolDefine a " +
           "WHERE (:filter IS NULL OR :filter = '' OR " +
           "LOWER(a.actionName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(a.displayName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(a.categoryCode) LIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<AiaToolDefineResponse> searchAll(@Param("filter") String filter, Pageable pageable);

    @Query("SELECT new com.ngv.aia_service.dto.response.AiaToolDefineResponse(" +
           "a.toolId, a.actionName, a.displayName, a.categoryCode, a.toolType, " +
           "a.toolDescription, a.preconditions, a.userParamSchema, a.outputSchema, " +
           "a.chainingHints, a.errorHandlingHints, a.usageExamples, a.backendActionCode, " +
           "a.systemParam, a.version, a.isActive, a.createdDate, a.createdBy, " +
           "a.modifiedDate, a.modifiedBy) " +
           "FROM AiaToolDefine a WHERE a.isActive = 1")
    List<AiaToolDefineResponse> findAllActiveTool();

    @Query("SELECT new com.ngv.aia_service.dto.response.AiaToolDefineResponse(" +
           "a.toolId, a.actionName, a.displayName, a.categoryCode, a.toolType, " +
           "a.toolDescription, a.preconditions, a.userParamSchema, a.outputSchema, " +
           "a.chainingHints, a.errorHandlingHints, a.usageExamples, a.backendActionCode, " +
           "a.systemParam, a.version, a.isActive, a.createdDate, a.createdBy, " +
           "a.modifiedDate, a.modifiedBy) " +
           "FROM AiaToolDefine a WHERE a.toolId = :toolId")
    Optional<AiaToolDefineResponse> findDetailById(@Param("toolId") Long toolId);

    Optional<AiaToolDefine> findByActionNameAndVersion(String actionName, String version);

    @Query("SELECT new com.ngv.aia_service.dto.response.AiaToolDefineResponse(" +
           "a.toolId, a.actionName, a.displayName, a.categoryCode, a.toolType, " +
           "a.toolDescription, a.preconditions, a.userParamSchema, a.outputSchema, " +
           "a.chainingHints, a.errorHandlingHints, a.usageExamples, a.backendActionCode, " +
           "a.systemParam, a.version, a.isActive, a.createdDate, a.createdBy, " +
           "a.modifiedDate, a.modifiedBy) " +
           "FROM AiaToolDefine a WHERE a.categoryCode = :categoryCode AND a.isActive = 1")
    List<AiaToolDefineResponse> findByCategoryCode(@Param("categoryCode") String categoryCode);

    Optional<AiaToolDefine> findByBackendActionCode(String backendActionCode);

    @Query("SELECT new com.ngv.aia_service.dto.response.AiaToolDefineResponse(" +
           "a.toolId, a.actionName, a.displayName, a.categoryCode, a.toolType, " +
           "a.toolDescription, a.preconditions, a.userParamSchema, a.outputSchema, " +
           "a.chainingHints, a.errorHandlingHints, a.usageExamples, a.backendActionCode, " +
           "a.systemParam, a.version, a.isActive, a.createdDate, a.createdBy, " +
           "a.modifiedDate, a.modifiedBy) " +
           "FROM AiaToolDefine a WHERE a.toolType = :toolType AND a.isActive = 1")
    List<AiaToolDefineResponse> findByToolType(@Param("toolType") String toolType);

    @Query("SELECT new com.ngv.aia_service.dto.response.AiaToolDefineResponse(" +
           "a.toolId, a.actionName, a.displayName, a.categoryCode, a.toolType, " +
           "a.toolDescription, a.preconditions, a.userParamSchema, a.outputSchema, " +
           "a.chainingHints, a.errorHandlingHints, a.usageExamples, a.backendActionCode, " +
           "a.systemParam, a.version, a.isActive, a.createdDate, a.createdBy, " +
           "a.modifiedDate, a.modifiedBy) " +
           "FROM AiaToolDefine a WHERE " +
           "(:categoryCode IS NULL OR a.categoryCode = :categoryCode) AND " +
           "(:toolType IS NULL OR a.toolType = :toolType) AND " +
           "(:isActive IS NULL OR a.isActive = :isActive) AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(a.actionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.displayName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<AiaToolDefineResponse> searchWithCriteria(@Param("categoryCode") String categoryCode,
                                                  @Param("toolType") String toolType,
                                                  @Param("isActive") Integer isActive,
                                                  @Param("keyword") String keyword,
                                                  Pageable pageable);

    boolean existsByActionNameAndVersion(String actionName, String version);

    @Query("SELECT COUNT(a) FROM AiaToolDefine a WHERE a.actionName = :actionName AND a.version = :version AND a.toolId != :toolId")
    Integer checkActionNameVersionExistForUpdate(@Param("actionName") String actionName, 
                                                @Param("version") String version, 
                                                @Param("toolId") Long toolId);

    @Query("SELECT new com.ngv.aia_service.dto.response.AiaToolDefineResponse(" +
           "a.toolId, a.actionName, a.displayName, a.categoryCode, a.toolType, " +
           "a.toolDescription, a.preconditions, a.userParamSchema, a.outputSchema, " +
           "a.chainingHints, a.errorHandlingHints, a.usageExamples, a.backendActionCode, " +
           "a.systemParam, a.version, a.isActive, a.createdDate, a.createdBy, " +
           "a.modifiedDate, a.modifiedBy) " +
           "FROM AiaToolDefine a ORDER BY a.createdDate DESC")
    List<AiaToolDefineResponse> findAllForExport();
}
