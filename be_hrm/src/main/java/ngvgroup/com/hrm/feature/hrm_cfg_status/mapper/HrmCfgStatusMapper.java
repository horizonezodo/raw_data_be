package ngvgroup.com.hrm.feature.hrm_cfg_status.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.hrm.feature.hrm_cfg_status.dto.HrmCfgStatusOptionDto;
import ngvgroup.com.hrm.feature.hrm_cfg_status.model.HrmCfgStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HrmCfgStatusMapper extends BaseMapper<HrmCfgStatusOptionDto, HrmCfgStatus> {
}
