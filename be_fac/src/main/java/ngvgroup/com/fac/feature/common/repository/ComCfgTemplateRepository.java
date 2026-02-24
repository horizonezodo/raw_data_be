package ngvgroup.com.fac.feature.common.repository;

import ngvgroup.com.fac.feature.common.dto.TemplateResDto;
import ngvgroup.com.fac.feature.common.model.ComCfgTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComCfgTemplateRepository extends JpaRepository<ComCfgTemplate, Long> {
    @Query("SELECT new ngvgroup.com.fac.feature.common.dto.TemplateResDto(" +
            "c.templateCode, c.fileName, c.description, c.filePath, c.fileSize, c.mappingFilePath) " +
            "FROM ComCfgTemplate c " +
            "WHERE c.isActive = 1 AND (" +
            ":keyword IS NULL OR :keyword = '' OR " +
            "LOWER(c.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.fileName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<TemplateResDto> searchAllTemplate(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT new ngvgroup.com.fac.feature.common.dto.TemplateResDto(
                    c.templateCode, c.fileName, c.description, c.filePath, c.fileSize, c.mappingFilePath
                    )
            FROM ComCfgTemplate c
            WHERE c.isActive = 1 AND
            c.templateCode = :templateCode
            """)
    Optional<TemplateResDto> findByTemplateCode(@Param("templateCode") String templateCode);

    @Query("""
            SELECT a.templateCode
            FROM ComCfgTemplate a
            LEFT JOIN FacCfgVoucherPrint b ON b.templateCode = a.templateCode
            LEFT JOIN FacTxnAcctEntry c ON c.voucherTypeCode = b.voucherTypeCode
            WHERE c.processInstanceCode = :processInstanceCode
            """)
    String findTemplateCodeByProcess(@Param("processInstanceCode") String processInstanceCode);

    @Query("""
            select new ngvgroup.com.fac.feature.common.dto.TemplateResDto(
                a.templateCode, a.fileName, a.description, a.filePath, a.fileSize, a.mappingFilePath
            )
            FROM ComCfgTemplate a
            LEFT JOIN FacCfgVoucherPrint b ON b.templateCode = a.templateCode
            LEFT JOIN FacTxnAcctEntry c ON c.voucherTypeCode = b.voucherTypeCode
            WHERE c.voucherTypeCode = :voucherTypeCode
            """)
    TemplateResDto findTemplate(@Param("voucherTypeCode") String voucherTypeCode);
}
