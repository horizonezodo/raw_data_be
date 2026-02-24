package com.naas.admin_service.features.common.service;

import java.util.List;

import com.naas.admin_service.features.common.dto.CtgComCommonDTO;

public interface CtgComCommonService {

    List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode);
}
