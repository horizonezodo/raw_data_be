package ngvgroup.com.fac.feature.fac_cfg_acct_entry.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacCfgAcctEntryRepository extends BaseRepository<FacCfgAcctEntry> {

    @Query("""
                select distinct new ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryResDTO(
                    c.id, c.entryTypeCode, c.entryName, c.description, c.entrySeqNo, c.entryCode,
                    c.ledgerType, c.entryDir )
                from FacCfgAcctEntry c
                where c.acctProcessCode = :acctProcessCode
                      AND c.orgCode = :orgCode
                order by c.entrySeqNo asc
            """)
    Page<FacCfgAcctEntryResDTO> getListDetail(@Param("acctProcessCode") String acctProcessCode,
                                              @Param("orgCode") String orgCode,
                                              Pageable pageable);

    List<FacCfgAcctEntry> findByAcctProcessCode(String acctProcessCode);

    void deleteByAcctProcessCode(String acctProcessCode);
}
