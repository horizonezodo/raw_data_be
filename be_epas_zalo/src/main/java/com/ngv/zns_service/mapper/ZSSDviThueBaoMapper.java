package com.ngv.zns_service.mapper;

import com.ngv.zns_service.dto.request.ZSSDviThueBaoRequest;
import com.ngv.zns_service.dto.response.ZSSDviThueBaoResponse;
import com.ngv.zns_service.model.entity.ZSSDviThueBao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZSSDviThueBaoMapper extends BaseMapper<ZSSDviThueBaoRequest, ZSSDviThueBaoResponse, ZSSDviThueBao> {
}

