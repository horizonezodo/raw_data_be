package com.ngv.zns_service.service;

import com.ngv.zns_service.dto.response.SelectBoxDto;

import java.util.List;
import java.util.Map;

public interface CommonService {
    Map<String, String> listCommon(String maDmuc);
    List<SelectBoxDto> getListMdichSdung();
}
