package ngvgroup.com.rpt.features.ctgcfgstatkpi.mapper;

import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatKpiDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.model.CtgCfgStatKpi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CtgCfgStatKpiMapper extends BaseMapper<CtgCfgStatKpiDto,CtgCfgStatKpi> {
    CtgCfgStatKpiMapper INSTANCE= Mappers.getMapper(CtgCfgStatKpiMapper.class);
}
