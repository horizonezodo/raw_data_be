package ngvgroup.com.rpt.features.ctgcfgstat.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscorekpiresult.CtgCfgStatScoreKpiResultDto;
import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatScoreKpiResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CtgCfgStatScoreKpiResultMapper extends BaseMapper<CtgCfgStatScoreKpiResultDto, CtgCfgStatScoreKpiResult> {
    CtgCfgStatScoreKpiResultMapper INSTANCE= Mappers.getMapper(CtgCfgStatScoreKpiResultMapper.class);
}
