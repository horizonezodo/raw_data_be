package ngvgroup.com.crm.features.crm_cfg_project_type.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeDto;
import ngvgroup.com.crm.features.crm_cfg_project_type.model.CrmCfgProjectType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CrmCfgProjectTypeMapper extends BaseMapper<CrmCfgProjectTypeDto, CrmCfgProjectType> {
}