package com.naas.admin_service.features.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.naas.admin_service.features.category.dto.TemplateResDto;
import com.naas.admin_service.features.category.model.ComCfgTemplate;

public interface ComCfgTemplateRepository extends JpaRepository<ComCfgTemplate, Long> {

       @Query("SELECT new com.naas.admin_service.features.category.dto.TemplateResDto(" +
                     "c.templateCode, c.fileName, c.description, c.filePath, c.fileSize, c.mappingFilePath) " +
                     "FROM ComCfgTemplate c " +
                     "WHERE c.isActive = 1 AND (" +
                     ":keyword IS NULL OR :keyword = '' OR " +
                     "LOWER(c.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(c.fileName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
       Page<TemplateResDto> searchListTemplate(@Param("keyword") String keyword, Pageable pageable);

       @Query("SELECT c FROM ComCfgTemplate c WHERE c.isActive = 1 AND c.templateCode = :templateCode")
       Optional<ComCfgTemplate> findEntityByTemplateCode(@Param("templateCode") String templateCode);

       @Query("SELECT new com.naas.admin_service.features.category.dto.TemplateResDto(" +
                     "c.templateCode, c.fileName, c.description, c.filePath, c.fileSize) " +
                     "FROM ComCfgTemplate c " +
                     "WHERE c.isActive = 1 AND c.templateCode = :templateCode")
       TemplateResDto findDtoByTemplateCode(@Param("templateCode") String templateCode);

       @Query("SELECT new com.naas.admin_service.features.category.dto.TemplateResDto(" +
                     "c.templateCode, c.fileName, c.description, c.filePath, c.fileSize, c.mappingFilePath) " +
                     "FROM ComCfgTemplate c WHERE c.isActive = 1 AND " +
                     "(:templateCode IS NULL OR c.templateCode = :templateCode) AND " +
                     "(:fileName IS NULL OR c.fileName = :fileName) AND " +
                     "(:description IS NULL OR c.description = :description)")
       List<TemplateResDto> searchToExportExcel(@Param("templateCode") String templateCode,
                     @Param("fileName") String fileName,
                     @Param("description") String description);
}
