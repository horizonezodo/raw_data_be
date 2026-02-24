package ngvgroup.com.ibm.feature.common.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.ibm.feature.common.dto.ComCfgBaseIntDto;
import ngvgroup.com.ibm.feature.common.model.ComCfgBaseInt;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComCfgBaseIntMapper extends BaseMapper<ComCfgBaseIntDto, ComCfgBaseInt> {
}