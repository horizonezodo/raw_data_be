package ngvgroup.com.fac.feature.fac_cfg_acct_entry.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctProcessResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacCfgAcctProcessRepository extends BaseRepository<FacCfgAcctProcess> {
    @Query("""
                select distinct new ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctProcessResDTO(
                    p.orgCode, o.orgName, p.moduleCode, b.moduleName, p.processTypeCode,
                    type.processTypeName, p.isActive, p.modifiedBy, p.acctProcessCode )
                from FacCfgAcctProcess p
                left join ComInfOrganization o
                on p.orgCode = o.orgCode
                left join ComCfgProcessType type
                on p.processTypeCode = type.processTypeCode
                left join ComCfgBusModule b
                on b.moduleCode = p.moduleCode
                where ( :keyword IS NULL OR
                                LOWER(o.orgName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(p.moduleCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(type.processTypeName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                            ) AND
                      ( :orgCode IS NULL OR
                                LOWER(p.orgCode) LIKE LOWER(CONCAT('%', :orgCode, '%'))
                            ) AND
                      ( :moduleCodes IS NULL OR p.moduleCode = :moduleCodes) AND
                      ( :processTypeCodes IS NULL OR p.processTypeCode = :processTypeCodes) AND
                      p.isActive = 1
                order by p.isActive desc
            """)
    Page<FacCfgAcctProcessResDTO> getListGeneral(@Param("keyword") String keyword,
                                                @Param("orgCode") String orgCode,
                                                @Param("moduleCodes") String moduleCodes,
                                                @Param("processTypeCodes") String processTypeCodes,
                                                Pageable pageable);

    @Query("""
                select distinct new ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctProcessResDTO(
                    p.orgCode, o.orgName, p.moduleCode, b.moduleName, p.processTypeCode,
                    type.processTypeName, p.isActive, p.modifiedBy, p.acctProcessCode )
                from FacCfgAcctProcess p
                left join ComInfOrganization o
                on p.orgCode = o.orgCode
                left join ComCfgProcessType type
                on p.processTypeCode = type.processTypeCode
                left join ComCfgBusModule b
                on b.moduleCode = p.moduleCode
                where ( :keyword IS NULL OR
                                LOWER(o.orgName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(p.moduleCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                                LOWER(type.processTypeName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                            ) AND
                      ( :orgCode IS NULL OR
                                LOWER(p.orgCode) LIKE LOWER(CONCAT('%', :orgCode, '%'))
                            ) AND
                      ( :moduleCodes IS NULL OR p.moduleCode IN :moduleCodes) AND
                      ( :processTypeCodes IS NULL OR p.processTypeCode IN :processTypeCodes) AND
                      p.isActive = 1
                order by p.isActive desc
            """)
    Page<FacCfgAcctProcessResDTO> getListGeneralByFilter(@Param("keyword") String keyword,
                                                         @Param("orgCode") String orgCode,
                                                         @Param("moduleCodes") List<String> moduleCodes,
                                                         @Param("processTypeCodes") List<String> processTypeCodes,
                                                         Pageable pageable);

    Optional<FacCfgAcctProcess> findByProcessTypeCodeAndOrgCode(
            @Param("processTypeCode") String processTypeCode,
            @Param("orgCode") String orgCode);

    @Modifying
    @Query("""
              DELETE FROM FacCfgAcctProcess p
              WHERE p.processTypeCode = :processTypeCode
                    and p.orgCode = :orgCode
            """)
    void deleteProcess(
            @Param("processTypeCode") String processTypeCode,
            @Param("orgCode") String orgCode);

    @Query("""
                select distinct new ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctProcessResDTO(
                    p.orgCode, o.orgName, p.moduleCode, b.moduleName, p.processTypeCode, type.processTypeName,
                    p.isActive, p.modifiedBy, p.acctProcessCode)
                from FacCfgAcctProcess p
                left join ComInfOrganization o
                on p.orgCode = o.orgCode
                left join ComCfgProcessType type
                on p.processTypeCode = type.processTypeCode
                left join ComCfgBusModule b
                on b.moduleCode = p.moduleCode
                where p.isActive = 1
                order by p.isActive desc
            """)
    List<FacCfgAcctProcessResDTO> exportToExcel();

    List<FacCfgAcctProcess> findByProcessTypeCode(
            @Param("processTypeCode") String processTypeCode);
}
