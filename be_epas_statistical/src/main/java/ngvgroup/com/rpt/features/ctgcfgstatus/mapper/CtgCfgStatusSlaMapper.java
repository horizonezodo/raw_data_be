package ngvgroup.com.rpt.features.ctgcfgstatus.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusSlaDto;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatusSla;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatusSlaMapper extends BaseMapper<CtgCfgStatusSlaDto, CtgCfgStatusSla> {
    CtgCfgStatusSlaMapper INSTANCE = Mappers.getMapper(CtgCfgStatusSlaMapper.class);
}
