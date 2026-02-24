package ngvgroup.com.rpt.features.ctgcfgstatscorerank.mapper;

import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreTypeDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CtgCfgStatScoreTypeMapper extends BaseMapper<CtgCfgStatScoreTypeDto,CtgCfgStatScoreType> {
    CtgCfgStatScoreTypeMapper INSTANCE= Mappers.getMapper(CtgCfgStatScoreTypeMapper.class);
}
