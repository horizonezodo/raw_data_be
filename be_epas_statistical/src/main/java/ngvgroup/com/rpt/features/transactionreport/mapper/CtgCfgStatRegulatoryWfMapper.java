package ngvgroup.com.rpt.features.transactionreport.mapper;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO;
import ngvgroup.com.rpt.features.transactionreport.model.CtgCfgStatRegulatoryWf;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatRegulatoryWfMapper extends BaseMapper<CtgCfgStatRegulatoryTypeDTO, CtgCfgStatRegulatoryWf> {
    CtgCfgStatRegulatoryWfMapper INSTANCE = Mappers.getMapper(CtgCfgStatRegulatoryWfMapper.class);

    @Mapping(target = "id", ignore = true) // Bỏ qua id
    void updateEntityFromDto(CtgCfgStatRegulatoryTypeDTO dto, @MappingTarget CtgCfgStatRegulatoryWf entity);
}
