package com.ngv.zns_service.service;

import com.ngv.zns_service.dto.response.MauZNS.DvuMauZnsDto;

import java.util.List;

public interface ZSSDvuMauZNSService {
    List<DvuMauZnsDto> syncData(String maDvi);
}
