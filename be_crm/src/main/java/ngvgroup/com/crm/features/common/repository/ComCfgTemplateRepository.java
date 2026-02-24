package ngvgroup.com.crm.features.common.repository;
import ngvgroup.com.crm.features.common.dto.TemplateResDto;
import ngvgroup.com.crm.features.common.model.ComCfgTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComCfgTemplateRepository extends JpaRepository<ComCfgTemplate, Long> {

       @Query("SELECT new ngvgroup.com.crm.features.common.dto.TemplateResDto(" +
                     "c.templateCode, c.fileName, c.description, c.filePath, c.fileSize, c.mappingFilePath) " +
                     "FROM ComCfgTemplate c " +
                     "WHERE c.isActive = 1 AND c.isDelete = 0 AND (" +
                     ":keyword IS NULL OR :keyword = '' OR " +
                     "LOWER(c.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(c.fileName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
       Page<TemplateResDto> searchAllTemplate(@Param("keyword") String keyword, Pageable pageable);

       @Query("""
        SELECT new ngvgroup.com.crm.features.common.dto.TemplateResDto(
                c.templateCode, c.fileName, c.description, c.filePath, c.fileSize, c.mappingFilePath
                )
        FROM ComCfgTemplate c
        WHERE c.isActive = 1 AND c.isDelete = 0 AND
        c.templateCode = :templateCode
        """)
       Optional<TemplateResDto> findByTemplateCode(@Param("templateCode") String templateCode);
}
