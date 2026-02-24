package com.naas.admin_service.features.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.naas.admin_service.features.common.dto.CtgComCommonDTO;
import com.naas.admin_service.features.common.repository.CtgComCommonRepository;
import com.naas.admin_service.features.common.service.CtgComCommonService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CtgComCommonServiceImpl implements CtgComCommonService {

    private final CtgComCommonRepository ctgComCommonRepository;

    @Override
    public List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode) {
        return ctgComCommonRepository.listCommon(commonTypeCode);
    }
}
