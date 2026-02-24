package com.ngv.zns_service.mapper;

import com.ngv.zns_service.dto.request.ZSSAppRequest;
import com.ngv.zns_service.dto.response.ZSSAppResponse;
import com.ngv.zns_service.model.entity.ZssApp;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZSSAppMapper extends BaseMapper <ZSSAppRequest, ZSSAppResponse, ZssApp>{
}
