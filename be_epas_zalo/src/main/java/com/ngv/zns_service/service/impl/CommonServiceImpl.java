package com.ngv.zns_service.service.impl;

import com.ngv.zns_service.dto.response.SelectBoxDto;
import com.ngv.zns_service.model.entity.ZSSDmucCtiet;
import com.ngv.zns_service.repository.ZSSDmucCtietRepository;
import com.ngv.zns_service.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final ZSSDmucCtietRepository zssDmucCtietRepository;
    @Override
    public Map<String, String> listCommon(String maDmuc) {
        List<ZSSDmucCtiet> dmucCtiets = zssDmucCtietRepository.listCommon(maDmuc);
        return dmucCtiets.stream()
                .collect(Collectors.toMap(ZSSDmucCtiet::getMaDmucCtiet, ZSSDmucCtiet::getTenDmucCtiet));
    }

    @Override
    public List<SelectBoxDto> getListMdichSdung() {
        return zssDmucCtietRepository.findMdichSdungSelectBox();
    }
}
