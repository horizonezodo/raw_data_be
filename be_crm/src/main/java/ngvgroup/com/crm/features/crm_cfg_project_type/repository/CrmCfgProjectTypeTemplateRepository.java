package ngvgroup.com.crm.features.crm_cfg_project_type.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeTemplateDto;
import ngvgroup.com.crm.features.crm_cfg_project_type.model.CrmCfgProjectTypeTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CrmCfgProjectTypeTemplateRepository extends BaseRepository<CrmCfgProjectTypeTemplate> {

    @Query("""
    SELECT new ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeTemplateDto(
        c.id,
        c.templateCode,
        c.orgCode,
        c.projectTypeCode,
        ct.fileName
    )
    FROM CrmCfgProjectTypeTemplate c
    LEFT JOIN ComCfgTemplate ct ON c.templateCode = ct.templateCode
    WHERE c.isDelete = 0
      AND (
            :keyword IS NULL
            OR LOWER(c.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.projectTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(ct.fileName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
      AND (c.projectTypeCode=:projectTypeCode)
""")
    Page<CrmCfgProjectTypeTemplateDto> searchAllByProjectTypeCode(@Param("keyword") String keyword,@Param("projectTypeCode") String projectTypeCode, Pageable pageable);

    List<CrmCfgProjectTypeTemplate> getAllByProjectTypeCode(@Param("projectTypeCode") String projectTypeCode);

    void deleteAllByProjectTypeCode(@Param("projectTypeCode") String projectTypeCode);
}
