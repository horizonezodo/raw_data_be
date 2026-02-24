package ngvgroup.com.crm.features.crm_cfg_project_type.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeTemplateDto;

import ngvgroup.com.crm.features.crm_cfg_project_type.model.CrmCfgProjectTypeTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CrmCfgProjectTypeTemplateMapper extends BaseMapper<CrmCfgProjectTypeTemplateDto, CrmCfgProjectTypeTemplate> {
}