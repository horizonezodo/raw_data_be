package com.ngv.zns_service.mapper;

import com.ngv.zns_service.dto.request.ZSSCtaoGtriTsoRequest;
import com.ngv.zns_service.dto.response.ZSSCtaoGtriTsoResponse;
import com.ngv.zns_service.model.entity.ZSSCtaoGtriTso;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZSSCtaoGtriTsoMapper extends BaseMapper<ZSSCtaoGtriTsoRequest, ZSSCtaoGtriTsoResponse, ZSSCtaoGtriTso>{
}
