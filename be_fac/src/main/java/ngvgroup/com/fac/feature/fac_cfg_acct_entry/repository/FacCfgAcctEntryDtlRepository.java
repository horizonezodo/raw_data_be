package ngvgroup.com.fac.feature.fac_cfg_acct_entry.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryDtlResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntryDtl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacCfgAcctEntryDtlRepository extends BaseRepository<FacCfgAcctEntryDtl> {
    @Query("""
                select new ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryDtlResDTO(
                    c.id, c.entryType, c.accSideType, c.accClassCode, a.accClassName, c.amtType, c.amtParamCode,
                    c.entrySeqNo, c.description )
                from FacCfgAcctEntryDtl c
                left join FacCfgAccClass a
                on c.accClassCode = a.accClassCode
                where c.entryCode = :entryCode
                order by c.entrySeqNo asc
            """)
    List<FacCfgAcctEntryDtlResDTO> getListEntryDetail(@Param("entryCode") String entryCode);

    @Query("""
                select d.accClassCode
                from FacCfgAcctEntryDtl d
                left join FacCfgAcctEntry e
                on d.entryCode = e.entryCode
                where d.entryType = :entryType
                    and e.entryTypeCode = :entryTypeCode
            """)
    List<String> getAccClassCode(@Param("entryTypeCode") String entryTypeCode,
                                 @Param("entryType") String entryType);

    @Modifying
    @Query("""
              DELETE FROM FacCfgAcctEntryDtl f
              WHERE f.entryCode = :entryCode
            """)
    void deleteByEntryCode(
            @Param("entryCode") String entryCode);

    List<FacCfgAcctEntryDtl> findByEntryCode(String entryCode);
}
