package com.ngv.zns_service.service.impl;

import com.ngv.zns_service.dto.response.MauZNS.DvuMauZnsDto;
import com.ngv.zns_service.repository.ZSSDvuMauZNSRepository;
import com.ngv.zns_service.service.ZSSDvuMauZNSService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZSSDvuMauZNSServiceImpl implements ZSSDvuMauZNSService {
    private final ZSSDvuMauZNSRepository zssDvuMauZNSRepository;

    public ZSSDvuMauZNSServiceImpl(ZSSDvuMauZNSRepository zssDvuMauZNSRepository) {
        this.zssDvuMauZNSRepository = zssDvuMauZNSRepository;
    }

    @Override
    public List<DvuMauZnsDto> syncData(String maDvi) {
        return zssDvuMauZNSRepository.findAllByMaDvi(maDvi).stream().map(
                zssDvuMauZNS -> {
                    DvuMauZnsDto dto = new DvuMauZnsDto();
                    BeanUtils.copyProperties(zssDvuMauZNS, dto);
                    return dto;
                }
        ).toList();
    }
}
