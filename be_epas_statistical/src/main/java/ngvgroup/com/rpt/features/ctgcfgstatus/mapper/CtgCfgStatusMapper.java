package ngvgroup.com.rpt.features.ctgcfgstatus.mapper;

import ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatusMapper extends BaseMapper<CtgCfgStatusDto, CtgCfgStatus> {
    CtgCfgStatusMapper INSTANCE = Mappers.getMapper(CtgCfgStatusMapper.class);

    void updateEntity(CtgCfgStatusDto dto, @MappingTarget CtgCfgStatus entity);
}
