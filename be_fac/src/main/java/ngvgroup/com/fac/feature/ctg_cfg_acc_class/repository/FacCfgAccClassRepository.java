package ngvgroup.com.fac.feature.ctg_cfg_acc_class.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacCfgAccClassRepository extends BaseRepository<FacCfgAccClass> {

    Optional<FacCfgAccClass> findByAccClassCode(String accClassCode);

    @Query("""
    SELECT new ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassDto(
        c.id,
        c.accClassCode,
        c.accClassName,
        c.accSideType,
        c.accNature,
        c.description
    )
    FROM FacCfgAccClass c
    WHERE 
        (:keyword IS NULL OR :keyword = '' 
         OR LOWER(c.accClassCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.accClassName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    ORDER BY c.modifiedDate DESC
    """)
    Page<FacCfgAccClassDto> searchAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT new ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassDto(
        c.id,
        c.accClassCode,
        c.accClassName,
        c.accNature,
        c.accSideType,
        c.description
    )
    FROM FacCfgAccClass c
    WHERE 
        (:keyword IS NULL OR :keyword = '' 
         OR LOWER(c.accClassCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.accClassName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    ORDER BY c.modifiedDate DESC
    """)
    List<FacCfgAccClassDto> exportToExcel(@Param("keyword") String keyword);

    boolean existsByAccClassCode(String accClassCode);

    @Query("""
    SELECT new ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassDto(
        c.id,
        c.accClassCode,
        c.accClassName,
        c.accNature,
        c.accSideType,
        c.description
    )
    FROM FacCfgAccClass c
    WHERE c.accSideType = :accSideType
    """)
    List<FacCfgAccClassDto> findAllByAccSideType(@Param("accSideType") String accSideType);
}
