package ngvgroup.com.crm.features.crm_cfg_project_type.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeDto;
import ngvgroup.com.crm.features.crm_cfg_project_type.model.CrmCfgProjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrmCfgProjectTypeRepository extends BaseRepository<CrmCfgProjectType> {

    @Query("""
    SELECT new ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeDto(
        p.id,
        p.projectTypeCode,
        p.projectTypeName,
        p.orgCode,
        p.description
    )
    FROM CrmCfgProjectType p
    WHERE (:keyword IS NULL OR :keyword = ''
         OR LOWER(p.projectTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(p.projectTypeName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    ORDER BY p.modifiedDate DESC
    """)
    Page<CrmCfgProjectTypeDto> search(@Param("keyword") String keyword, Pageable pageable);


    @Query("""
    SELECT new ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeDto(
        p.id,
        p.projectTypeCode,
        p.projectTypeName,
        p.orgCode,
        p.description
    )
    FROM CrmCfgProjectType p
    WHERE (:keyword IS NULL OR :keyword = ''
         OR LOWER(p.projectTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(p.projectTypeName) LIKE LOWER(CONCAT('%', :keyword, '%'))
         OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    ORDER BY p.modifiedDate DESC
    """)
    List<CrmCfgProjectTypeDto> exportToExcel(@Param("keyword") String keyword);

    boolean existsByProjectTypeCode(String projectTypeCode);
}