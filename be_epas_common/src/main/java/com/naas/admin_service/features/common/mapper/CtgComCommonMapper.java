package com.naas.admin_service.features.common.mapper;


import com.naas.admin_service.features.common.dto.CtgComCommonDTO;
import com.naas.admin_service.features.common.model.CtgComCommon;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgComCommonMapper extends BaseMapper<CtgComCommonDTO, CtgComCommon> {
}
