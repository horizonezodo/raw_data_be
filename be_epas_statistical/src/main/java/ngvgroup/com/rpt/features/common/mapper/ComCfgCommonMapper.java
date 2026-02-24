package ngvgroup.com.rpt.features.common.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.common.dto.ComCfgCommonDto;
import ngvgroup.com.rpt.features.common.model.ComCfgCommon;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ComCfgCommonMapper extends BaseMapper<ComCfgCommonDto, ComCfgCommon> {
    ComCfgCommonMapper INSTANCE= Mappers.getMapper(ComCfgCommonMapper.class);
}
