package ngvgroup.com.hrm.feature.common.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.hrm.feature.common.dto.CtgComCommonDTO;
import ngvgroup.com.hrm.feature.common.model.CtgComCommon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgComCommonMapper extends BaseMapper<CtgComCommonDTO, CtgComCommon> {
}
