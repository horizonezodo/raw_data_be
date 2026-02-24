package ngvgroup.com.fac.feature.regulated_account.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.regulated_account.dto.CoaAccExportProjection;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccResDTO;
import ngvgroup.com.fac.feature.regulated_account.model.CtgCfgCoaAcc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CtgCfgCoaAccRepository extends BaseRepository<CtgCfgCoaAcc> {

    Optional<CtgCfgCoaAcc> findByAccCoaCodeAndOrgCodeAndIsInternal(String code, String orgCode,String isInternal);

    @Query("""
            SELECT new ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccResDTO(
                c.id,
                c.accCoaCode,
                c.accCoaName,
                com.commonName,
                c.accLevel,
                c.parentCode,
                c.coaVersionCode,
                '',
                c.accType
            )FROM CtgCfgCoaAcc c
            JOIN CtgCfgCommon com ON com.commonValue=c.accNature
            WHERE  c.coaVersionCode = :versionCode
            AND (c.accType IN :accTypes) 
            
            AND (
                :keyword is null or
                 LOWER(c.accCoaCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                 LOWER(com.commonName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                 LOWER(c.accCoaName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
            )
            AND (c.isInternal=:isInternal)
            ORDER by c.accCoaCode asc
        """)
    Page<CtgCfgCoaAccResDTO> pageCoaAcc(
            @Param("keyword")String keyword,
            @Param("versionCode")String versionCode,
            @Param("accTypes")List<String> accTypes,
            @Param("isInternal") String isInternal,
            Pageable pageable
    );
    @Query("""
        SELECT new ngvgroup.com.fac.feature.regulated_account.dto.CoaAccExportProjection(
            c.accCoaCode,
            c.accCoaName,
            com.commonName
           )
        FROM CtgCfgCoaAcc c
         JOIN CtgCfgCommon com ON com.commonValue=c.accNature
       
          WHERE c.isInternal IN :isInternals
         
       """)
    List<CoaAccExportProjection> exportCoaAcc( @Param("isInternals")List<String> isInternals);


    CtgCfgCoaAcc getByAccCoaCode(String accCoaCode);

    List<CtgCfgCoaAcc> getAllByIsInternal(String isInternal);

    List<CtgCfgCoaAcc> getAllByIsInternalAndAccCoaCode(String isInternal,String accCoaCode);

    CtgCfgCoaAcc getByAccCoaCodeAndIsInternal(String accCoaCode,String isInternal);
}
