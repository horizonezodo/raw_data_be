package ngvgroup.com.rpt.features.ctgcfgstat.mapper;

import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscoregroupkpi.CtgCfgStatScoreGroupKpiDto;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatScoreGroupKpi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CtgCfgStatScoreGroupKpiMapper extends BaseMapper<CtgCfgStatScoreGroupKpiDto, CtgCfgStatScoreGroupKpi> {
    CtgCfgStatScoreGroupKpiMapper INSTANCE= Mappers.getMapper(CtgCfgStatScoreGroupKpiMapper.class);
}
