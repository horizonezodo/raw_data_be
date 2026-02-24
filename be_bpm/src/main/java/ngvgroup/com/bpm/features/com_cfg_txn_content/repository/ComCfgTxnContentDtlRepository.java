package ngvgroup.com.bpm.features.com_cfg_txn_content.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.bpm.features.com_cfg_txn_content.dto.ComCfgTxnContentDtlDto;
import ngvgroup.com.bpm.features.com_cfg_txn_content.model.ComCfgTxnContentDtl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ComCfgTxnContentDtlRepository extends BaseRepository<ComCfgTxnContentDtl> {
    @Query("""
             SELECT new ngvgroup.com.bpm.features.com_cfg_txn_content.dto.ComCfgTxnContentDtlDto(
                 d.id,
                 d.orgCode,
                 d.contentDtlCode,
                 cvt.commonName,
                 d.contentValue,
                 d.sortNumber,
                 d.formatMask,
                 d.length
             )
             FROM ComCfgTxnContentDtl d
             LEFT JOIN CtgComCommon cvt
                 ON cvt.commonCode = d.contentValueType
                AND cvt.commonTypeCode = 'CM118'
             WHERE d.contentCode = :contentCode
               AND d.isActive = 1
               AND (:keyword IS NULL OR :keyword = ''
                    OR LOWER(d.contentDtlCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(cvt.commonName) like LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(d.contentValue) LIKE LOWER(CONCAT('%', :keyword, '%'))
            
               )
            """)
    Page<ComCfgTxnContentDtlDto> search(
            @Param("contentCode") String contentCode,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Modifying
    @Transactional
    void deleteAllByContentCode(String contentCode);

    @Query("""
                SELECT d
                FROM ComCfgTxnContentDtl d
                WHERE d.contentCode IN :contentCodes
            """)
    List<ComCfgTxnContentDtl> findByContentCodes(
            @Param("contentCodes") List<String> contentCodes
    );

    List<ComCfgTxnContentDtl> findAllByContentCode(String contentCode);

    boolean existsByContentCodeAndContentDtlCodeAndIsActive(String contentCode, String contentDtlCode, Integer isActive);
}