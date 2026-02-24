package com.naas.category_service.service.impl;

import com.naas.category_service.dto.ComCommon.CommonDto;
import com.naas.category_service.repository.CtgComCommonRepository;
import com.naas.category_service.service.CtgComCommonService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgComCommonServiceImpl implements CtgComCommonService {
    private final CtgComCommonRepository ctgComCommonRepository;

    @Override
    public List<CommonDto> listCommon(String commonTypeCode) {
        return ctgComCommonRepository.listCommon(commonTypeCode);
    }


}
