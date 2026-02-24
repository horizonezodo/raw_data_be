package ngvgroup.com.hrm.feature.cfg_org_unit.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitDTO;
import ngvgroup.com.hrm.feature.cfg_org_unit.model.HrmCfgOrgUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HrmCfgOrgUnitMapper extends BaseMapper<HrmCfgOrgUnitDTO, HrmCfgOrgUnit> {

    @Mapping(target = "orgUnitCode", ignore = true)
    void updateEntityFromDto(HrmCfgOrgUnitDTO dto, @MappingTarget HrmCfgOrgUnit entity);
}
