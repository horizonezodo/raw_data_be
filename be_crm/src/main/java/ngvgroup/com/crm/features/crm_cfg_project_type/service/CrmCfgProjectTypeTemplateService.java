package ngvgroup.com.crm.features.crm_cfg_project_type.service;

import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeTemplateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrmCfgProjectTypeTemplateService {

    Page<CrmCfgProjectTypeTemplateDto> searchAllByProjectTypeCode(String keyword,String projectTypeCode, Pageable pageable);

    void create(List<CrmCfgProjectTypeTemplateDto> crmCfgProjectTypeTemplateDtos);

    void update(String projectTypeCode,List<CrmCfgProjectTypeTemplateDto> crmCfgProjectTypeTemplateDtos);

    void deleteAllByProjectTypeCode(String projectTypeCode);
}
