package ngvgroup.com.bpm.features.com_cfg_txn_content.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.bpm.features.com_cfg_txn_content.dto.ComCfgTxnContentDto;
import ngvgroup.com.bpm.features.com_cfg_txn_content.model.ComCfgTxnContent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgTxnContentRepository extends BaseRepository<ComCfgTxnContent> {

    List<ComCfgTxnContentDto> findAllByModuleCode(@Param("processTypeCode") String moduleCode);

    @Query("""
                SELECT new ngvgroup.com.bpm.features.com_cfg_txn_content.dto.ComCfgTxnContentDto(
                    c.id,
                    c.orgCode,
                    c.contentCode,
                    c.contentText,
                    c.contentName,
                    c.moduleCode,
                    c.length
                )
                FROM ComCfgTxnContent c
                WHERE
                    c.isActive = 1

            """)
    List<ComCfgTxnContentDto> search();

    boolean existsByContentCode(String contentCode);

    Optional<ComCfgTxnContent> findByContentCode(String contentCode);

}