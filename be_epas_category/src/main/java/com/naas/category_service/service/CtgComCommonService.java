package com.naas.category_service.service;

import com.naas.category_service.dto.ComCommon.CommonDto;


import java.util.List;

public interface CtgComCommonService {
    List<CommonDto> listCommon(String commonTypeCode);


}
