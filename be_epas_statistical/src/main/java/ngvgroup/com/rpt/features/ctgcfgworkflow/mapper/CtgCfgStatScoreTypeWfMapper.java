package ngvgroup.com.rpt.features.ctgcfgworkflow.mapper;

import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgStatScoreTypeWfDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgStatScoreTypeWf;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface CtgCfgStatScoreTypeWfMapper extends BaseMapper<CtgCfgStatScoreTypeWfDto, CtgCfgStatScoreTypeWf> {
    CtgCfgStatScoreTypeWfMapper INSTANCE= Mappers.getMapper(CtgCfgStatScoreTypeWfMapper.class);
}
