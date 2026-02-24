package ngvgroup.com.fac.feature.ctg_cfg_acc_class.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapResDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClassCoaMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacCfgAccClassCoaMapRepository extends BaseRepository<FacCfgAccClassCoaMap> {
    Optional<FacCfgAccClassCoaMap> findByAccClassCodeAndOrgCode(String accClassCode, String orgCode);
    List<FacCfgAccClassCoaMap> findByOrgCodeAndAccClassCodeIn(String orgCode, Collection<String> accClassCodes);

    @Query("""
    SELECT new ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapDto(
        fm.id,
        fm.orgCode,
        fm.effectiveDate,
        fm.coaVersionCode,
        fm.accClassCode,
        fm.accCoaCode,
        fm.debtGroupCode,
        fm.currencyCode,
        fm.channelCode,
        fa.accCoaName
    )
    FROM FacCfgAccClassCoaMap fm
    JOIN CtgCfgCoaAcc fa ON fm.accCoaCode = fa.accCoaCode
    WHERE (
        :keyword IS NULL OR
        LOWER(fm.orgCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(fm.accClassCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(fm.accCoaCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(fm.debtGroupCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(fm.currencyCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(fm.channelCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(fa.accCoaName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    AND fm.accClassCode=:accClassCode
""")
    Page<FacCfgAccClassCoaMapDto> searchAll(
            @Param("keyword") String keyword,
            @Param("accClassCode") String accClassCode,
            Pageable pageable
    );

    @Query("""
    SELECT new ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapResDto(
        fm.orgCode,
        fm.accClassCode,
        fm.accCoaCode
    )
    FROM FacCfgAccClassCoaMap fm
    WHERE (:orgCode is null or fm.orgCode = :orgCode) AND
          (:accClassCode is null or fm.accClassCode = :accClassCode)
""")
    Optional<FacCfgAccClassCoaMapResDto> findByOrgCodeAndAccClassCode(
            @Param("orgCode") String orgCode,
            @Param("accClassCode") String accClassCode);

    FacCfgAccClassCoaMap getByOrgCodeAndAccClassCode(String orgCode,String accClassCode);

    List<FacCfgAccClassCoaMap> getAllByAccClassCode(String accClassCode);
}
