package ngvgroup.com.crm.features.crm_cfg_project_type.service.impl;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeTemplateDto;
import ngvgroup.com.crm.features.crm_cfg_project_type.mapper.CrmCfgProjectTypeTemplateMapper;
import ngvgroup.com.crm.features.crm_cfg_project_type.model.CrmCfgProjectTypeTemplate;
import ngvgroup.com.crm.features.crm_cfg_project_type.repository.CrmCfgProjectTypeTemplateRepository;
import ngvgroup.com.crm.features.crm_cfg_project_type.service.CrmCfgProjectTypeTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class CrmCfgProjectTypeTemplateServiceImpl extends BaseServiceImpl<CrmCfgProjectTypeTemplate,CrmCfgProjectTypeTemplateDto> implements CrmCfgProjectTypeTemplateService{

    private CrmCfgProjectTypeTemplateRepository crmCfgProjectTypeTemplateRepository;


    public CrmCfgProjectTypeTemplateServiceImpl( CrmCfgProjectTypeTemplateRepository crmCfgProjectTypeTemplateRepository, CrmCfgProjectTypeTemplateMapper crmCfgProjectTypeTemplateMapper) {
        super(crmCfgProjectTypeTemplateRepository, crmCfgProjectTypeTemplateMapper);
        this.crmCfgProjectTypeTemplateRepository = crmCfgProjectTypeTemplateRepository;
    }

    @Override
    public Page<CrmCfgProjectTypeTemplateDto> searchAllByProjectTypeCode( String keyword,String projectTypeCode, Pageable pageable){
        return crmCfgProjectTypeTemplateRepository.searchAllByProjectTypeCode(keyword,projectTypeCode,pageable);
    }

    @Override
    public void create(List<CrmCfgProjectTypeTemplateDto> crmCfgProjectTypeTemplateDtos){
        for (CrmCfgProjectTypeTemplateDto c: crmCfgProjectTypeTemplateDtos) {
            create(c);
        }
    }

    @Override
    @Transactional
    public void update(String projectTypeCode,List<CrmCfgProjectTypeTemplateDto> crmCfgProjectTypeTemplateDtos){
        List<CrmCfgProjectTypeTemplate> oldList =
                crmCfgProjectTypeTemplateRepository.getAllByProjectTypeCode(projectTypeCode);

        Set<Long> requestIds = crmCfgProjectTypeTemplateDtos.stream()
                .map(CrmCfgProjectTypeTemplateDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (CrmCfgProjectTypeTemplate old : oldList) {
            if (!requestIds.contains(old.getId())) {
                delete(old.getId());
            }
        }

        for (CrmCfgProjectTypeTemplateDto c : crmCfgProjectTypeTemplateDtos) {
            if (c.getId() != null) {
                update(c.getId(), c);
            } else {
                create(c);
            }
        }
    }

    @Override
    @Transactional
    public void deleteAllByProjectTypeCode(String projectTypeCode){
       crmCfgProjectTypeTemplateRepository.deleteAllByProjectTypeCode(projectTypeCode);
    }

}
